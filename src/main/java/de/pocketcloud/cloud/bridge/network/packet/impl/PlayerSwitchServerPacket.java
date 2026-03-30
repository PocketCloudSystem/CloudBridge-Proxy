package de.pocketcloud.cloud.bridge.network.packet.impl;

import de.pocketcloud.cloud.bridge.network.packet.CloudPacket;
import de.pocketcloud.cloud.bridge.network.packet.CloudboundPacket;
import de.pocketcloud.cloud.bridge.network.packet.util.PacketData;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public final class PlayerSwitchServerPacket extends CloudPacket implements CloudboundPacket {

    private String player;
    private String newServer;

    public PlayerSwitchServerPacket(String player, String newServer) {
        this.player = player;
        this.newServer = newServer;
    }

    @Override
    public void handle() {}

    @Override
    public void encodePayload(PacketData packetData) {
        packetData.writeAll(player, newServer);
    }

    public static PlayerSwitchServerPacket create(String player, String newServer) {
        return new PlayerSwitchServerPacket(player, newServer);
    }
}
