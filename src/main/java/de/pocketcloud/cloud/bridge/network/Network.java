package de.pocketcloud.cloud.bridge.network;

import de.pocketcloud.cloud.bridge.CloudBridge;
import de.pocketcloud.cloud.bridge.event.network.*;
import de.pocketcloud.cloud.bridge.exception.NetworkException;
import de.pocketcloud.cloud.bridge.exception.PacketTooLargeException;
import de.pocketcloud.cloud.bridge.network.packet.CloudboundPacket;
import de.pocketcloud.cloud.bridge.network.packet.PacketPool;
import de.pocketcloud.cloud.bridge.network.packet.ResponsePacket;
import de.pocketcloud.cloud.bridge.network.packet.UnhandledPacket;
import de.pocketcloud.cloud.bridge.network.packet.util.PacketSerializer;
import de.pocketcloud.cloud.bridge.network.request.RequestManager;
import de.pocketcloud.cloud.bridge.network.util.Address;
import de.pocketcloud.cloud.bridge.traffic.TrafficMonitor;
import de.pocketcloud.cloud.bridge.traffic.TrafficMonitorManager;
import de.pocketcloud.cloud.bridge.util.CloudEnvironmentConfig;
import dev.waterdog.waterdogpe.ProxyServer;
import lombok.Getter;

import java.io.IOException;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public final class Network extends Thread {

    @Getter
    private static Network instance;

    @Getter
    private final Address address;
    private final ConcurrentLinkedQueue<UnhandledPacket> buffer;
    private Selector selector;
    private SocketChannel channel;

    private final AtomicBoolean connected = new AtomicBoolean(false);
    private final AtomicBoolean running = new AtomicBoolean(false);

    @Getter
    private int packetSizeLimit;
    @Getter
    private boolean encryptionEnabled;
    @Getter
    private String authenticationKey;

    public Network(Address address) {
        super("Network-Thread");
        this.address = address;
        this.buffer = new ConcurrentLinkedQueue<>();

        PacketPool.initialize();
        packetSizeLimit = CloudEnvironmentConfig.getNetworkPacketSizeLimit();
        encryptionEnabled = CloudEnvironmentConfig.isNetworkEncryptionEnabled();
        authenticationKey = CloudEnvironmentConfig.getNetworkAuthKey();

        instance = this;
        setDaemon(true);
    }

    public void init() throws IOException {
        if (connected.get()) throw new IllegalStateException("Socket has already been established");

        this.selector = Selector.open();

        this.channel = SocketChannel.open();
        this.channel.configureBlocking(false);
        this.channel.setOption(StandardSocketOptions.TCP_NODELAY, true);
        this.channel.connect(address.toInetSocketAddress());

        this.channel.register(selector, SelectionKey.OP_CONNECT);

        connected.set(true);
        CloudBridge.getInstance().getLogger().info("Connecting to Cloud via TCP at {}...", address);
    }

    @Override
    public void run() {
        running.set(true);
        ByteBuffer streamBuffer = ByteBuffer.allocate(1024 * 1024);

        while (running.get() && connected.get()) {
            try {
                if (selector.select(50) == 0) continue;

                var keys = selector.selectedKeys().iterator();
                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove();

                    if (!key.isValid()) continue;

                    if (key.isConnectable()) {
                        if (channel.finishConnect()) {
                            key.interestOps(SelectionKey.OP_READ);
                            CloudBridge.getInstance().getLogger().info("§aTCP Connection established!");
                        }
                    } else if (key.isReadable()) {
                        int bytesRead = channel.read(streamBuffer);
                        if (bytesRead == -1) {
                            close();
                            ProxyServer.getInstance().shutdown();
                            return;
                        }

                        processBuffer(streamBuffer);
                    }
                }
            } catch (ClosedSelectorException e) {
                break;
            } catch (IOException e) {
                if (running.get()) {
                    CloudBridge.getInstance().getLogger().error("Network error: {}", e.getMessage());
                    close();
                    ProxyServer.getInstance().shutdown();
                }
            }
        }
    }

    private void processBuffer(ByteBuffer buffer) {
        buffer.flip();

        while (buffer.remaining() >= 4) {
            buffer.mark();
            int length = buffer.getInt();

            if (length > packetSizeLimit || length < 0) {
                CloudBridge.getInstance().getLogger().error("§cReceived packet exceeding limit: {} bytes. Disconnecting...", length);
                return;
            }

            if (buffer.remaining() < length) {
                buffer.reset();
                break;
            }

            byte[] payload = new byte[length];
            buffer.get(payload);

            this.buffer.offer(new UnhandledPacket(payload, address, length));
        }

        buffer.compact();
    }

    public void tick() {
        UnhandledPacket unhandledPacket;
        while ((unhandledPacket = buffer.poll()) != null) {
            if (!connected.get()) return;

            TrafficMonitorManager.getInstance().pushBytes(TrafficMonitorManager.TRAFFIC_NETWORK, unhandledPacket.bytes(), TrafficMonitor.REGULAR_MODE_IN);

            NetworkPacketReceivePreProcessEvent ev = new NetworkPacketReceivePreProcessEvent(this, new String(unhandledPacket.buffer(), StandardCharsets.UTF_8), encryptionEnabled);
            ProxyServer.getInstance().getEventManager().callEvent(ev);
            if (ev.isCancelled()) continue;

            try {
                var packet = unhandledPacket.buildCloudPacket(encryptionEnabled, authenticationKey);
                if (packet != null) {
                    NetworkPacketReceiveEvent receiveEvent = new NetworkPacketReceiveEvent(this, packet);
                    ProxyServer.getInstance().getEventManager().callEvent(receiveEvent);
                    if (!receiveEvent.isCancelled()) {
                        packet.handle();
                        if (packet instanceof ResponsePacket response) {
                            RequestManager.getInstance().resolve(response);
                            RequestManager.getInstance().remove(response.getRequestId());
                        }
                    }
                }
            } catch (Exception e) {
                CloudBridge.getInstance().getLogger().error("Packet handle error", e);
            }
        }
    }

    public void sendPacket(CloudboundPacket packet) {
        if (!connected.get() || !channel.isConnected()) throw new NetworkException("Client not connected to cloud");

        NetworkPacketPreSendEvent preSendEvent = new NetworkPacketPreSendEvent(this, packet);
        ProxyServer.getInstance().getEventManager().callEvent(preSendEvent);
        if (preSendEvent.isCancelled()) return;

        byte[] encodedData = PacketSerializer.encode(packet, encryptionEnabled, authenticationKey);
        if (encodedData.length > packetSizeLimit) {
            ProxyServer.getInstance().getEventManager().callEvent(new NetworkPacketTooLargeEvent(this, packet, encodedData.length, new String(encodedData, StandardCharsets.UTF_8)));
            throw new PacketTooLargeException(packet, encodedData.length, packetSizeLimit);
        }

        try {
            ByteBuffer sendBuf = ByteBuffer.allocate(4 + encodedData.length);
            sendBuf.putInt(encodedData.length);
            sendBuf.put(encodedData);
            sendBuf.flip();

            while (sendBuf.hasRemaining()) {
                if (channel.write(sendBuf) == 0) {
                    Thread.yield();
                }
            }

            TrafficMonitorManager.getInstance().pushBytes(TrafficMonitorManager.TRAFFIC_NETWORK, encodedData.length, TrafficMonitor.REGULAR_MODE_OUT);
            ProxyServer.getInstance().getEventManager().callEvent(new NetworkPacketSentEvent(this, packet, true));
        } catch (IOException e) {
            throw new NetworkException(e.getMessage());
        }
    }

    public void close() {
        if (!connected.get()) return;
        running.set(false);
        connected.set(false);
        ProxyServer.getInstance().getEventManager().callEvent(new NetworkCloseEvent(this));
        try {
            if (selector != null) selector.close();
            if (channel != null) channel.close();
        } catch (IOException ignored) {}
    }

    public boolean isConnected() {
        return connected.get() && channel != null && channel.isConnected();
    }
}