package de.pocketcloud.cloud.bridge.network.packet.impl;

import de.pocketcloud.cloud.bridge.network.packet.CloudboundPacket;
import de.pocketcloud.cloud.bridge.network.packet.CloudPacket;
import de.pocketcloud.cloud.bridge.network.packet.util.PacketData;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public final class PlayerDisconnectPacket extends CloudPacket implements CloudboundPacket {
    
    private String player;

    public PlayerDisconnectPacket(String player) {
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

    public static PlayerDisconnectPacket create(String player) {
        return new PlayerDisconnectPacket(player);
    }
}