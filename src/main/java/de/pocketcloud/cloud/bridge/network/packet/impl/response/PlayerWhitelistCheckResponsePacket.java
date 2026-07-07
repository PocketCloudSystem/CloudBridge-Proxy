package de.pocketcloud.cloud.bridge.network.packet.impl.response;

import de.pocketcloud.cloud.bridge.network.packet.ResponsePacket;
import de.pocketcloud.network.packet.data.PacketData;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public final class PlayerWhitelistCheckResponsePacket extends ResponsePacket {

    private boolean whitelisted;

    public PlayerWhitelistCheckResponsePacket(boolean whitelisted) {
        this.whitelisted = whitelisted;
    }

    @Override
    public void decodePayload(PacketData packetData) {
        whitelisted = Boolean.TRUE.equals(packetData.readBool());
    }

    @Override
    public void handle() {}

    public static PlayerWhitelistCheckResponsePacket create(boolean whitelisted) {
        return new PlayerWhitelistCheckResponsePacket(whitelisted);
    }
}