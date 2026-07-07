package de.pocketcloud.cloud.bridge.network.packet.impl;

import de.pocketcloud.cloud.bridge.api.cache.NotificationListCache;
import de.pocketcloud.network.packet.ClientboundPacket;
import de.pocketcloud.network.packet.CloudboundPacket;
import de.pocketcloud.cloud.bridge.network.packet.CloudPacket;
import de.pocketcloud.cloud.bridge.network.packet.type.NotificationType;
import de.pocketcloud.network.packet.data.PacketData;
import dev.waterdog.waterdogpe.ProxyServer;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@NoArgsConstructor
public final class CloudNotificationPacket extends CloudPacket implements ClientboundPacket, CloudboundPacket {
    
    private NotificationType notificationType;
    private Map<String, Object> args;
    
    public CloudNotificationPacket(NotificationType notificationType, Map<String, Object> args) {
        this.notificationType = notificationType;
        this.args = args;
    }
    
    @Override
    public void handle() {
        assert notificationType.getLangKey() != null;
        String message = notificationType.getLangKey().translate(args);
        for (String player : NotificationListCache.getAll()) {
            ProxiedPlayer p = ProxyServer.getInstance().getPlayer(player);
            if (p != null) p.sendMessage(message);
        }
    }
    
    @Override
    public void encodePayload(PacketData packetData) {
        packetData.writeAll(notificationType, args);
    }
    
    @Override
    public void decodePayload(PacketData packetData) {
        notificationType = packetData.readEnum(NotificationType.class);
        args = packetData.readMap();
    }

    public static CloudNotificationPacket create(NotificationType notificationType, Map<String, Object> args) {
        return new CloudNotificationPacket(notificationType, args);
    }
}