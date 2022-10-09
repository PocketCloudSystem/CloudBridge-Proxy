package de.pocketcloud.cloudbridge.network.packet.impl.request;

import de.pocketcloud.cloudbridge.network.packet.RequestPacket;
import de.pocketcloud.cloudbridge.network.packet.content.PacketContent;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CloudServerStopRequestPacket extends RequestPacket {

    private String server;

    public CloudServerStopRequestPacket(String server) {
        super();
        this.server = server;
    }

    @Override
    protected void encodePayload(PacketContent content) {
        super.encodePayload(content);
        content.put(server);
    }

    @Override
    protected void decodePayload(PacketContent content) {
        super.decodePayload(content);
        server = content.readString();
    }

    public String getServer() {
        return server;
    }
}