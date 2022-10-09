package de.pocketcloud.cloudbridge.network.packet.impl.normal;

import de.pocketcloud.cloudbridge.api.server.status.ServerStatus;
import de.pocketcloud.cloudbridge.network.packet.CloudPacket;
import de.pocketcloud.cloudbridge.network.packet.content.PacketContent;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CloudServerStatusChangePacket extends CloudPacket {

    private ServerStatus newStatus;

    public CloudServerStatusChangePacket(ServerStatus newStatus) {
        this.newStatus = newStatus;
    }

    @Override
    protected void encodePayload(PacketContent content) {
        super.encodePayload(content);
        content.putServerStatus(newStatus);
    }

    @Override
    protected void decodePayload(PacketContent content) {
        super.decodePayload(content);
        newStatus = content.readServerStatus();
    }

    public ServerStatus getNewStatus() {
        return newStatus;
    }
}
