package de.pocketcloud.cloudbridge.network.packet.impl.request;

import de.pocketcloud.cloudbridge.network.packet.RequestPacket;
import de.pocketcloud.cloudbridge.network.packet.data.PacketData;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CheckPlayerMaintenanceRequestPacket extends RequestPacket {

    private String player;

    public CheckPlayerMaintenanceRequestPacket(String player) {
        this.player = player;
    }

    @Override
    protected void encodePayload(PacketData packetData) {
        super.encodePayload(packetData);
        packetData.write(player);
    }

    @Override
    protected void decodePayload(PacketData packetData) {
        super.decodePayload(packetData);
        player = packetData.readString();
    }

}