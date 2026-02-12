package de.pocketcloud.cloud.bridge.network.packet;

import de.pocketcloud.cloud.bridge.network.packet.impl.*;
import de.pocketcloud.cloud.bridge.network.packet.impl.request.PlayerNotificationCheckRequestPacket;
import de.pocketcloud.cloud.bridge.network.packet.impl.request.PlayerWhitelistCheckRequestPacket;
import de.pocketcloud.cloud.bridge.network.packet.impl.request.ServerHandshakeRequestPacket;
import de.pocketcloud.cloud.bridge.network.packet.impl.response.PlayerNotificationCheckResponsePacket;
import de.pocketcloud.cloud.bridge.network.packet.impl.response.PlayerWhitelistCheckResponsePacket;
import de.pocketcloud.cloud.bridge.network.packet.impl.response.ServerHandshakeResponsePacket;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class PacketPool {
    private static PacketPool instance;
    
    private final Map<String, Supplier<CloudPacket>> packets = new HashMap<>();
    
    private PacketPool() {
        init();
    }
    
    public static void initialize() {
        instance = new PacketPool();
    }
    
    public static PacketPool getInstance() {
        if (instance == null) {
            initialize();
        }
        return instance;
    }
    
    private void init() {
        register(ServerHandshakeRequestPacket.class, ServerHandshakeRequestPacket::new);
        register(ServerHandshakeResponsePacket.class, ServerHandshakeResponsePacket::new);
        register(DisconnectPacket.class, DisconnectPacket::new);
        register(CloudNotificationPacket.class, CloudNotificationPacket::new);
        register(KeepAlivePacket.class, KeepAlivePacket::new);
        register(CommandExecutePacket.class, CommandExecutePacket::new);
        register(CommandAnswerPacket.class, CommandAnswerPacket::new);
        register(LanguageSyncPacket.class, LanguageSyncPacket::new);
        register(LibrarySyncPacket.class, LibrarySyncPacket::new);
        register(ModuleSyncPacket.class, ModuleSyncPacket::new);
        register(TemplateSyncPacket.class, TemplateSyncPacket::new);
        register(ServerSyncPacket.class, ServerSyncPacket::new);
        register(ServerGroupSyncPacket.class, ServerGroupSyncPacket::new);
        register(PlayerSyncPacket.class, PlayerSyncPacket::new);
        register(PlayerConnectPacket.class, PlayerConnectPacket::new);
        register(PlayerDisconnectPacket.class, PlayerDisconnectPacket::new);
        register(PlayerKickPacket.class, PlayerKickPacket::new);
        register(PlayerSwitchServerPacket.class, PlayerSwitchServerPacket::new);
        register(ProxyRegisterServerPacket.class, ProxyRegisterServerPacket::new);
        register(ProxyUnregisterServerPacket.class, ProxyUnregisterServerPacket::new);
        register(PlayerNotificationCheckRequestPacket.class, PlayerNotificationCheckRequestPacket::new);
        register(PlayerNotificationCheckResponsePacket.class, PlayerNotificationCheckResponsePacket::new);
        register(PlayerWhitelistCheckRequestPacket.class, PlayerWhitelistCheckRequestPacket::new);
        register(PlayerWhitelistCheckResponsePacket.class, PlayerWhitelistCheckResponsePacket::new);
        register(PlayerUpdateNotificationStatePacket.class, PlayerUpdateNotificationStatePacket::new);
        register(MaintenanceListSyncPacket.class, MaintenanceListSyncPacket::new);
        register(NotificationListSyncPacket.class, NotificationListSyncPacket::new);
        register(ServerChangeStatusPacket.class, ServerChangeStatusPacket::new);
        register(CloudSyncServerStoragePacket.class, CloudSyncServerStoragePacket::new);
        register(ProxyPlayerTransferPacket.class, ProxyPlayerTransferPacket::new);
    }

    public void register(Class<? extends CloudPacket> packetClass, Supplier<CloudPacket> supplier) {
        String packetName = packetClass.getSimpleName();
        packets.put(packetName, supplier);
    }

    public CloudPacket get(String packetName) {
        Supplier<CloudPacket> supplier = packets.get(packetName);
        return supplier != null ? supplier.get() : null;
    }

    public Map<String, Supplier<CloudPacket>> getAll() {
        return new HashMap<>(packets);
    }
}