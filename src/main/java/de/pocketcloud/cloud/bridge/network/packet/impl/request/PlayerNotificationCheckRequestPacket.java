package de.pocketcloud.cloud.bridge.network.packet.impl.request;

import de.pocketcloud.cloud.bridge.network.packet.RequestPacket;
import de.pocketcloud.cloud.bridge.network.packet.util.PacketData;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PlayerNotificationCheckRequestPacket extends RequestPacket {

    private String player;

    public PlayerNotificationCheckRequestPacket(String player) {
        this.player = player;
    }

    @Override
    public void encodePayload(PacketData packetData) {
        packetData.writeAll(this.player);
    }
}