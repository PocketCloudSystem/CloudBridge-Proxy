package de.pocketcloud.cloudbridge.network.packet.pool;

import de.pocketcloud.cloudbridge.network.packet.CloudPacket;
import de.pocketcloud.cloudbridge.network.packet.impl.normal.*;
import de.pocketcloud.cloudbridge.network.packet.impl.request.*;
import de.pocketcloud.cloudbridge.network.packet.impl.response.*;
import dev.waterdog.waterdogpe.logger.MainLogger;

import java.util.HashMap;

public class PacketPool {

    private static PacketPool instance;

    private final HashMap<String, Class<? extends CloudPacket>> packets = new HashMap<>();

    public PacketPool() {
        instance = this;
        registerPacket(LoginRequestPacket.class);
        registerPacket(LoginResponsePacket.class);
        registerPacket(DisconnectPacket.class);
        registerPacket(KeepAlivePacket.class);
        registerPacket(CommandSendPacket.class);
        registerPacket(CommandSendAnswerPacket.class);
        registerPacket(ConsoleTextPacket.class);
        registerPacket(PlayerConnectPacket.class);
        registerPacket(PlayerDisconnectPacket.class);
        registerPacket(PlayerKickPacket.class);
        registerPacket(CloudServerSavePacket.class);
        registerPacket(CloudServerStatusChangePacket.class);
        registerPacket(PlayerSwitchServerPacket.class);
        registerPacket(TemplateSyncPacket.class);
        registerPacket(ServerSyncPacket.class);
        registerPacket(PlayerSyncPacket.class);
        registerPacket(CloudServerStartRequestPacket.class);
        registerPacket(CloudServerStartResponsePacket.class);
        registerPacket(CloudServerStopRequestPacket.class);
        registerPacket(CloudServerStopResponsePacket.class);
        registerPacket(CheckPlayerMaintenanceRequestPacket.class);
        registerPacket(CheckPlayerMaintenanceResponsePacket.class);
        registerPacket(CheckPlayerNotifyRequestPacket.class);
        registerPacket(CheckPlayerNotifyResponsePacket.class);
        registerPacket(ProxyRegisterServerPacket.class);
        registerPacket(ProxyUnregisterServerPacket.class);
    }

    public void registerPacket(Class<? extends CloudPacket> clazz) {
        packets.put(clazz.getSimpleName(), clazz);
    }

    public CloudPacket getPacketById(String pid) {
        if (!packets.containsKey(pid)) return null;
        Class<? extends CloudPacket> packetClass = packets.getOrDefault(pid, null);
        if (packetClass == null) return null;
        CloudPacket packet = null;
        try {
            packet = packetClass.newInstance();
        } catch (Exception e) {
            MainLogger.getLogger().throwing(e);
        }
        return packet;
    }

    public static PacketPool getInstance() {
        return instance;
    }
}
