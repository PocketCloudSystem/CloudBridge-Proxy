package de.pocketcloud.cloudbridge.network;

import de.pocketcloud.cloudbridge.CloudBridge;
import de.pocketcloud.cloudbridge.event.NetworkPacketSendEvent;
import de.pocketcloud.cloudbridge.network.packet.CloudPacket;
import de.pocketcloud.cloudbridge.network.packet.handler.PacketHandler;
import de.pocketcloud.cloudbridge.network.packet.handler.encode.PacketEncoder;
import de.pocketcloud.cloudbridge.network.packet.listener.PacketListener;
import de.pocketcloud.cloudbridge.network.packet.pool.PacketPool;
import de.pocketcloud.cloudbridge.network.request.RequestManager;
import dev.waterdog.waterdogpe.ProxyServer;
import dev.waterdog.waterdogpe.logger.MainLogger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

public class Network implements Runnable {

    private static Network instance;
    private final PacketPool packetPool;
    private final PacketListener packetListener;
    private final PacketHandler packetHandler;
    private final RequestManager requestManager;
    private final InetSocketAddress address;
    private DatagramSocket socket;
    private boolean connected = false;

    public Network(InetSocketAddress address) {
        instance = this;
        this.address = address;
        packetPool = new PacketPool();
        packetListener = new PacketListener();
        packetHandler = new PacketHandler();
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
                PacketHandler.getInstance().handle(buffer);
            }
        } while (connected);
    }

    public void connect() {
        if (connected) return;
        try {
            socket = new DatagramSocket();
            socket.connect(address);
            socket.setSendBufferSize(1024 * 1024 * 8);
            socket.setReceiveBufferSize(1024 * 1024 * 8);
            connected = true;
            MainLogger.getLogger().info("Successfully connected to §e" + address.toString() + "§r!");
            MainLogger.getLogger().info("§cWaiting for incoming packets...");
        } catch (SocketException e) {
            e.printStackTrace();
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
            e.printStackTrace();
        }
        return new String(packet.getData()).trim();
    }

    public void close() {
        if (!connected) return;
        connected = false;
        socket.disconnect();
        socket.close();
    }

    public boolean sendPacket(CloudPacket packet) {
        if (connected) {
            try {
                String json = PacketEncoder.encode(packet);
                NetworkPacketSendEvent ev = new NetworkPacketSendEvent(packet);
                ProxyServer.getInstance().getEventManager().callEvent(ev);

                if (!ev.isCancelled()) return write(json);
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    public PacketPool getPacketPool() {
        return packetPool;
    }

    public PacketListener getPacketListener() {
        return packetListener;
    }

    public PacketHandler getPacketHandler() {
        return packetHandler;
    }

    public RequestManager getRequestManager() {
        return requestManager;
    }

    public boolean isConnected() {
        return connected;
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    public DatagramSocket getSocket() {
        return socket;
    }

    public static Network getInstance() {
        return instance;
    }
}
