package de.pocketcloud.cloudbridge.network.packet.impl.normal;

import de.pocketcloud.cloudbridge.api.server.status.ServerStatus;
import de.pocketcloud.cloudbridge.network.packet.CloudPacket;
import de.pocketcloud.cloudbridge.network.packet.data.PacketData;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CloudServerStatusChangePacket extends CloudPacket {

    private ServerStatus newStatus;

    public CloudServerStatusChangePacket(ServerStatus newStatus) {
        this.newStatus = newStatus;
    }

    @Override
    protected void encodePayload(PacketData packetData) {
        super.encodePayload(packetData);
        packetData.writeServerStatus(newStatus);
    }

    @Override
    protected void decodePayload(PacketData packetData) {
        super.decodePayload(packetData);
        newStatus = packetData.readServerStatus();
    }

    @Override
    public void handle() {}

}
