package de.pocketcloud.cloud.bridge.network.packet.impl;

import de.pocketcloud.cloud.bridge.network.packet.ClientboundPacket;
import de.pocketcloud.cloud.bridge.network.packet.CloudboundPacket;
import de.pocketcloud.cloud.bridge.network.packet.CloudPacket;
import de.pocketcloud.cloud.bridge.network.packet.data.NotificationType;
import de.pocketcloud.cloud.bridge.network.packet.util.PacketData;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public final class CloudNotificationPacket extends CloudPacket implements ClientboundPacket, CloudboundPacket {
    
    private NotificationType notificationType;
    private List<Object> args;
    
    public CloudNotificationPacket(NotificationType notificationType, List<Object> args) {
        this.notificationType = notificationType;
        this.args = args;
    }
    
    @Override
    public void handle() {}
    
    @Override
    public void encodePayload(PacketData packetData) {
        packetData.writeAll(notificationType, args);
    }
    
    @Override
    public void decodePayload(PacketData packetData) {
        notificationType = packetData.readNotificationType();
        args = packetData.readArray();
    }

    public static CloudNotificationPacket create(NotificationType notificationType, List<Object> args) {
        return new CloudNotificationPacket(notificationType, args);
    }
}