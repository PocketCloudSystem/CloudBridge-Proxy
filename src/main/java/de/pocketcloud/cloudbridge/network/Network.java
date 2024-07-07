package de.pocketcloud.cloudbridge.network;

import de.pocketcloud.cloudbridge.CloudBridge;
import de.pocketcloud.cloudbridge.event.NetworkCloseEvent;
import de.pocketcloud.cloudbridge.event.NetworkConnectEvent;
import de.pocketcloud.cloudbridge.event.NetworkPacketReceiveEvent;
import de.pocketcloud.cloudbridge.event.NetworkPacketSendEvent;
import de.pocketcloud.cloudbridge.network.packet.CloudPacket;
import de.pocketcloud.cloudbridge.network.packet.ResponsePacket;
import de.pocketcloud.cloudbridge.network.packet.handler.PacketSerializer;
import de.pocketcloud.cloudbridge.network.packet.pool.PacketPool;
import de.pocketcloud.cloudbridge.network.request.RequestManager;
import de.pocketcloud.cloudbridge.util.GeneralSettings;
import de.pocketcloud.cloudbridge.util.Utils;
import dev.waterdog.waterdogpe.ProxyServer;
import dev.waterdog.waterdogpe.logger.MainLogger;
import lombok.Getter;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

public class Network implements Runnable {

    @Getter
    private static Network instance;
    @Getter
    private final PacketPool packetPool;
    @Getter
    private final RequestManager requestManager;
    @Getter
    private final InetSocketAddress address;
    @Getter
    private DatagramSocket socket;
    @Getter
    private boolean connected = false;

    public Network(InetSocketAddress address) {
        instance = this;
        this.address = address;
        this.packetPool = new PacketPool();
        requestManager = new RequestManager();

        MainLogger.getLogger().info("Try to connect to §e" + address.toString() + "§r...");
        connect();
        CloudBridge.getInstance().getThreadPool().submit(this);
    }

    @Override
    public void run() {
        do {
            String buffer;
            if ((buffer = read()) != null) {
                CloudPacket packet = PacketSerializer.decode(buffer);
                if (packet != null) {
                    NetworkPacketReceiveEvent ev = new NetworkPacketReceiveEvent(packet);
                    ProxyServer.getInstance().getEventManager().callEvent(ev);
                    if (ev.isCancelled()) return;
                    packet.handle();

                    if (packet instanceof ResponsePacket) {
                        RequestManager.getInstance().callThen(((ResponsePacket) packet));
                        RequestManager.getInstance().removeRequest(((ResponsePacket) packet).getRequestId());
                    }
                } else {
                    MainLogger.getLogger().warning("§cReceived an unknown packet from the cloud!");
                    MainLogger.getLogger().debug(GeneralSettings.isNetworkEncryptionEnabled() ? Utils.decompress(buffer.getBytes(StandardCharsets.UTF_8)) : buffer);
                }
            }
        } while (connected);
    }

    public void connect() {
        if (connected) return;
        try {
            ProxyServer.getInstance().getEventManager().callEvent(new NetworkConnectEvent(address));
            socket = new DatagramSocket();
            socket.connect(address);
            socket.setSendBufferSize(1024 * 1024 * 8);
            socket.setReceiveBufferSize(1024 * 1024 * 8);
            connected = true;
            MainLogger.getLogger().info("Successfully connected to §e" + address.toString() + "§r!");
            MainLogger.getLogger().info("§cWaiting for incoming packets...");
        } catch (SocketException e) {
            ProxyServer.getInstance().getLogger().error("Failed to connect", e);
        }
    }

    public boolean write(String buffer) {
        DatagramPacket packet = new DatagramPacket(buffer.getBytes(StandardCharsets.UTF_8), buffer.getBytes(StandardCharsets.UTF_8).length, address);
        try {
            socket.send(packet);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public String read() {
        if (!connected) return "";
        byte[] buffer = new byte[1024 * 1024 * 8];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        try {
            socket.receive(packet);
        } catch (IOException e) {
            ProxyServer.getInstance().getLogger().error("Failed to receive a packet", e);
        }
        return new String(packet.getData()).trim();
    }

    public void close() {
        if (!connected) return;
        connected = false;
        ProxyServer.getInstance().getEventManager().callEvent(new NetworkCloseEvent());
        socket.disconnect();
        socket.close();
    }

    public boolean sendPacket(CloudPacket packet) {
        if (connected) {
            try {
                String json = PacketSerializer.encode(packet);
                NetworkPacketSendEvent ev = new NetworkPacketSendEvent(packet);
                ProxyServer.getInstance().getEventManager().callEvent(ev);

                if (!ev.isCancelled()) return write(json);
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }
}