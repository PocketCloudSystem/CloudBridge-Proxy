package de.pocketcloud.cloudbridge.network.packet.impl.normal;

import de.pocketcloud.cloudbridge.api.server.status.ServerStatus;
import de.pocketcloud.cloudbridge.network.packet.CloudPacket;
import de.pocketcloud.cloudbridge.network.packet.content.PacketContent;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class LocalServerUpdatePacket extends CloudPacket {

    private String server;
    private ServerStatus newStatus;

    public LocalServerUpdatePacket(String server, ServerStatus newStatus) {
        this.server = server;
        this.newStatus = newStatus;
    }

    @Override
    protected void encodePayload(PacketContent content) {
        super.encodePayload(content);
        content.put(server);
        content.putServerStatus(newStatus);
    }

    @Override
    protected void decodePayload(PacketContent content) {
        super.decodePayload(content);
        server = content.readString();
        newStatus = content.readServerStatus();
    }

    public String getServer() {
        return server;
    }

    public ServerStatus getNewStatus() {
        return newStatus;
    }
}
