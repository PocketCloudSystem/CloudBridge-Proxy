package de.pocketcloud.cloud.bridge.network.packet.impl.request;

import de.pocketcloud.cloud.bridge.network.packet.RequestPacket;
import de.pocketcloud.network.packet.data.PacketData;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public final class PlayerWhitelistCheckRequestPacket extends RequestPacket {

    private String player;

    public PlayerWhitelistCheckRequestPacket(String player) {
        this.player = player;
    }

    @Override
    public void encodePayload(PacketData packetData) {
        packetData.writeAll(this.player);
    }
}