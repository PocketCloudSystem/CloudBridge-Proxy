package de.pocketcloud.cloudbridge.network.packet.impl.normal;

import de.pocketcloud.cloudbridge.network.packet.CloudPacket;
import de.pocketcloud.cloudbridge.network.packet.content.PacketContent;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class LocalServerUnregisterPacket extends CloudPacket {

    private String server;

    public LocalServerUnregisterPacket(String server) {
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
