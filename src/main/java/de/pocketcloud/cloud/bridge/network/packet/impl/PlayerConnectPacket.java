package de.pocketcloud.cloud.bridge.network.packet.impl;

import de.pocketcloud.cloud.bridge.api.object.player.CloudPlayer;
import de.pocketcloud.network.packet.CloudboundPacket;
import de.pocketcloud.cloud.bridge.network.packet.CloudPacket;
import de.pocketcloud.network.packet.data.PacketData;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public final class PlayerConnectPacket extends CloudPacket implements CloudboundPacket {
    
    private CloudPlayer player;

    public PlayerConnectPacket(CloudPlayer player) {
        this.player = player;
    }
    
    @Override
    public void handle() {}

    @Override
    public void encodePayload(PacketData packetData) {
        packetData.writeAll(player);
    }

    @Override
    public void decodePayload(PacketData packetData) {}

    public static PlayerConnectPacket create(CloudPlayer player) {
        return new PlayerConnectPacket(player);
    }
}