package de.pocketcloud.cloudbridge.network.packet.impl.normal;

import de.pocketcloud.cloudbridge.network.packet.CloudPacket;
import de.pocketcloud.cloudbridge.network.packet.data.PacketData;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PlayerDisconnectPacket extends CloudPacket {

    private String playerName;

    public PlayerDisconnectPacket(String playerName) {
        this.playerName = playerName;
    }

    @Override
    protected void encodePayload(PacketData packetData) {
        super.encodePayload(packetData);
        packetData.write(playerName);
    }

    @Override
    protected void decodePayload(PacketData packetData) {
        super.decodePayload(packetData);
        playerName = packetData.readString();
    }

    @Override
    public void handle() {}

}
