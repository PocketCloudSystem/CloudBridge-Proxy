package de.pocketcloud.cloud.bridge.network.packet.impl.response;

import de.pocketcloud.cloud.bridge.network.packet.ResponsePacket;
import de.pocketcloud.cloud.bridge.network.packet.util.PacketData;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public final class PlayerNotificationCheckResponsePacket extends ResponsePacket {
    
    private boolean enabled;
    
    public PlayerNotificationCheckResponsePacket(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void decodePayload(PacketData packetData) {
        enabled = Boolean.TRUE.equals(packetData.readBool());
    }

    @Override
    public void handle() {}
    
    public static PlayerNotificationCheckResponsePacket create(boolean enabled) {
        return new PlayerNotificationCheckResponsePacket(enabled);
    }
}