package de.pocketcloud.cloudbridge.network.packet.impl.normal;

import de.pocketcloud.cloudbridge.network.packet.CloudPacket;
import de.pocketcloud.cloudbridge.network.packet.content.PacketContent;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
public class LocalServerRegisterPacket extends CloudPacket {

    private Map<?,?> server;

    public LocalServerRegisterPacket(Map<?,?> server) {
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
        server = content.readMap();
    }

    public Map<?,?> getServer() {
        return server;
    }
}
