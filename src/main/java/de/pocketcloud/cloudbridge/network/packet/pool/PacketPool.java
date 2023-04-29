package de.pocketcloud.cloudbridge.network.packet.pool;

import de.pocketcloud.cloudbridge.network.packet.CloudPacket;
import de.pocketcloud.cloudbridge.network.packet.impl.normal.*;
import de.pocketcloud.cloudbridge.network.packet.impl.request.CheckPlayerMaintenanceRequestPacket;
import de.pocketcloud.cloudbridge.network.packet.impl.request.CloudServerStartRequestPacket;
import de.pocketcloud.cloudbridge.network.packet.impl.request.CloudServerStopRequestPacket;
import de.pocketcloud.cloudbridge.network.packet.impl.request.LoginRequestPacket;
import de.pocketcloud.cloudbridge.network.packet.impl.response.CheckPlayerMaintenanceResponsePacket;
import de.pocketcloud.cloudbridge.network.packet.impl.response.CloudServerStartResponsePacket;
import de.pocketcloud.cloudbridge.network.packet.impl.response.CloudServerStopResponsePacket;
import de.pocketcloud.cloudbridge.network.packet.impl.response.LoginResponsePacket;
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
        registerPacket(KeepALivePacket.class);
        registerPacket(CommandSendPacket.class);
        registerPacket(LocalPlayerRegisterPacket.class);
        registerPacket(LocalPlayerUpdatePacket.class);
        registerPacket(LocalPlayerUnregisterPacket.class);
        registerPacket(LocalServerRegisterPacket.class);
        registerPacket(LocalServerUpdatePacket.class);
        registerPacket(LocalServerUnregisterPacket.class);
        registerPacket(LocalTemplateRegisterPacket.class);
        registerPacket(LocalTemplateUpdatePacket.class);
        registerPacket(LocalTemplateUnregisterPacket.class);
        registerPacket(PlayerConnectPacket.class);
        registerPacket(PlayerDisconnectPacket.class);
        registerPacket(CloudServerStartRequestPacket.class);
        registerPacket(CloudServerStartResponsePacket.class);
        registerPacket(CloudServerStopRequestPacket.class);
        registerPacket(CloudServerStopResponsePacket.class);
        registerPacket(CloudServerSavePacket.class);
        registerPacket(CloudServerStatusChangePacket.class);
        registerPacket(ProxyRegisterServerPacket.class);
        registerPacket(ProxyUnregisterServerPacket.class);
        registerPacket(CloudPlayerSwitchServerPacket.class);
        registerPacket(PlayerKickPacket.class);
        registerPacket(CheckPlayerMaintenanceRequestPacket.class);
        registerPacket(CheckPlayerMaintenanceResponsePacket.class);
    }

    public void registerPacket(Class<? extends CloudPacket> clazz) {
        packets.put(clazz.getSimpleName(), clazz);
    }

    public CloudPacket getPacketById(String identifier) {
        if (!packets.containsKey(identifier)) return null;
        Class<? extends CloudPacket> packetClass = packets.get(identifier);
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
