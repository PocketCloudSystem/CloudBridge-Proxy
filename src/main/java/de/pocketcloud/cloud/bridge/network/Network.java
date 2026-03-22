package de.pocketcloud.cloud.bridge.network;

import de.pocketcloud.cloud.bridge.CloudBridge;
import de.pocketcloud.cloud.bridge.event.network.*;
import de.pocketcloud.cloud.bridge.network.packet.CloudboundPacket;
import de.pocketcloud.cloud.bridge.network.packet.PacketPool;
import de.pocketcloud.cloud.bridge.network.packet.ResponsePacket;
import de.pocketcloud.cloud.bridge.network.packet.UnhandledPacket;
import de.pocketcloud.cloud.bridge.network.packet.util.PacketSerializer;
import de.pocketcloud.cloud.bridge.network.request.RequestManager;
import de.pocketcloud.cloud.bridge.network.util.Address;
import de.pocketcloud.cloud.bridge.traffic.TrafficMonitor;
import de.pocketcloud.cloud.bridge.traffic.TrafficMonitorManager;
import de.pocketcloud.cloud.bridge.traffic.impl.NetworkTrafficMonitor;
import de.pocketcloud.cloud.bridge.util.CloudEnvironmentConfig;
import dev.waterdog.waterdogpe.ProxyServer;
import lombok.Getter;

import java.io.IOException;
import java.net.SocketException;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
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
    private DatagramChannel channel;

    private final AtomicBoolean connected = new AtomicBoolean(false);
    private final AtomicBoolean running = new AtomicBoolean(false);
    
    @Getter
    private boolean encryptionEnabled;
    @Getter
    private String authenticationKey;
    
    public Network(Address address) {
        super("Network-Thread");
        this.address = address;
        this.buffer = new ConcurrentLinkedQueue<>();
        
        PacketPool.initialize();
        encryptionEnabled = CloudEnvironmentConfig.isNetworkEncryptionEnabled();
        authenticationKey = CloudEnvironmentConfig.getNetworkAuthKey();

        instance = this;
        setDaemon(true);
    }

    public void init() throws IOException {
        if (connected.get()) throw new IllegalStateException("Socket has already been established");
        
        try {
            this.selector = Selector.open();

            this.channel = DatagramChannel.open();
            this.channel.setOption(StandardSocketOptions.SO_SNDBUF, 1024 * 1024 * 8);
            this.channel.setOption(StandardSocketOptions.SO_RCVBUF, 1024 * 1024 * 8);
            this.channel.configureBlocking(false);
            this.channel.connect(address.toInetSocketAddress());

            this.channel.register(selector, SelectionKey.OP_READ);

            connected.set(true);

            CloudBridge.getInstance().getLogger().info("Successfully connected to {}!", address);
            CloudBridge.getInstance().getLogger().info("§cWaiting for incoming packets...");
        } catch (SocketException e) {
            throw new IOException("Failed to create socket: " + e.getMessage(), e);
        }
    }

    @Override
    public void run() {
        running.set(true);
        ByteBuffer receiveBuffer = ByteBuffer.allocate(65535);

        while (running.get() && connected.get()) {
            try {
                selector.select(50);

                if (!selector.isOpen()) break;

                var keys = selector.selectedKeys().iterator();
                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove();

                    if (!key.isValid()) continue;

                    if (key.isReadable()) {
                        DatagramChannel ch = (DatagramChannel) key.channel();

                        receiveBuffer.clear();
                        ch.read(receiveBuffer);

                        receiveBuffer.flip();
                        byte[] data = new byte[receiveBuffer.remaining()];
                        receiveBuffer.get(data);

                        UnhandledPacket unhandledPacket = new UnhandledPacket(data, address, data.length);
                        buffer.offer(unhandledPacket);
                    }
                }
            } catch (ClosedSelectorException e) {
                break;
            }  catch (IOException e) {
                if (running.get()) {
                    CloudBridge.getInstance().getLogger().error("Error receiving packet", e);
                }
            }
        }

        try {
            channel.close();
            selector.close();
        } catch (IOException ignored) {}
    }

    public void tick() {
        UnhandledPacket unhandledPacket;
        while ((unhandledPacket = buffer.poll()) != null) {
            if (!connected.get()) return;

            TrafficMonitorManager.getInstance().pushBytes(TrafficMonitorManager.TRAFFIC_NETWORK, unhandledPacket.bytes(), TrafficMonitor.REGULAR_MODE_IN);
            TrafficMonitorManager.getInstance().callHandlers(
                    TrafficMonitorManager.TRAFFIC_NETWORK,
                    TrafficMonitor.REGULAR_MODE_IN,
                    unhandledPacket.buffer(), unhandledPacket.bytes(), unhandledPacket.address()
            );

            NetworkPacketReceivePreProcessEvent networkPacketReceivePreProcessEvent = new NetworkPacketReceivePreProcessEvent(this, unhandledPacket.address(), new String(unhandledPacket.buffer(), StandardCharsets.UTF_8), encryptionEnabled);
            ProxyServer.getInstance().getEventManager().callEvent(networkPacketReceivePreProcessEvent);
            if (networkPacketReceivePreProcessEvent.isCancelled()) return;

            try {
                var packet = unhandledPacket.buildCloudPacket(encryptionEnabled, authenticationKey);
                if (packet != null) {
                    TrafficMonitorManager.getInstance().callHandlers(
                            TrafficMonitorManager.TRAFFIC_NETWORK,
                            NetworkTrafficMonitor.parsePacketMode(NetworkTrafficMonitor.NETWORK_MODE_PACKET_IN, packet.getClass()),
                            packet, unhandledPacket.address()
                    );

                    NetworkPacketReceiveEvent networkPacketReceiveEvent = new NetworkPacketReceiveEvent(this, unhandledPacket.address(), packet);
                    ProxyServer.getInstance().getEventManager().callEvent(networkPacketReceiveEvent);
                    if (networkPacketReceiveEvent.isCancelled()) return;

                    packet.handle();
                    
                    if (packet instanceof ResponsePacket responsePacket) {
                        RequestManager.getInstance().resolve(responsePacket);
                        RequestManager.getInstance().remove(responsePacket.getRequestId());
                    }
                } else {
                    CloudBridge.getInstance().getLogger().warn("§cReceived an unknown packet from the cloud!");
                }
            } catch (Exception e) {
                CloudBridge.getInstance().getLogger().error("Failed to handle packet from {}!", unhandledPacket.address());
                CloudBridge.getInstance().getLogger().error(e);
            }
        }
    }

    public boolean sendPacket(CloudboundPacket packet) {
        if (!connected.get()) return false;

        NetworkPacketPreSendEvent networkPacketPreSendEvent = new NetworkPacketPreSendEvent(this, address, packet);
        ProxyServer.getInstance().getEventManager().callEvent(networkPacketPreSendEvent);
        if (networkPacketPreSendEvent.isCancelled()) return false;

        byte[] encodedData = PacketSerializer.encode(packet, encryptionEnabled, authenticationKey);
        if (encodedData == null) return false;
        String encodedDataAsString = new String(encodedData, StandardCharsets.UTF_8);

        try {
            if (encodedDataAsString.length() > 65507) {
                ProxyServer.getInstance().getEventManager().callEvent(new NetworkPacketTooLargeEvent(this, address, packet, encodedDataAsString.length(), encodedDataAsString));
                return false;
            }

            ByteBuffer buffer = ByteBuffer.wrap(encodedData);
            channel.write(buffer);

            TrafficMonitorManager.getInstance().pushBytes(TrafficMonitorManager.TRAFFIC_NETWORK, encodedDataAsString.length(), TrafficMonitor.REGULAR_MODE_OUT);
            TrafficMonitorManager.getInstance().callHandlers(
                    TrafficMonitorManager.TRAFFIC_NETWORK,
                    TrafficMonitor.REGULAR_MODE_OUT,
                    encodedDataAsString, encodedDataAsString.length(), encodedDataAsString
            );

            TrafficMonitorManager.getInstance().callHandlers(
                    TrafficMonitorManager.TRAFFIC_NETWORK,
                    NetworkTrafficMonitor.parsePacketMode(NetworkTrafficMonitor.NETWORK_MODE_PACKET_OUT, packet.getClass()),
                    packet, address, true
            );

            ProxyServer.getInstance().getEventManager().callEvent(new NetworkPacketSentEvent(this, address, packet, true));
            return true;
        } catch (IOException e) {
            CloudBridge.getInstance().getLogger().error("Failed to send packet {}: {}", packet.getName(), e.getMessage());
            CloudBridge.getInstance().getLogger().error(e);
            return false;
        }
    }

    public void close() {
        if (!connected.get()) return;

        running.set(false);
        connected.set(false);

        ProxyServer.getInstance().getEventManager().callEvent(new NetworkCloseEvent(this));

        try {
            if (channel != null && channel.isOpen()) channel.close();
            if (selector != null && selector.isOpen()) {
                selector.wakeup();
                selector.close();
            }
        } catch (IOException ignored) {}
    }
    
    public boolean isConnected() {
        return connected.get();
    }
}