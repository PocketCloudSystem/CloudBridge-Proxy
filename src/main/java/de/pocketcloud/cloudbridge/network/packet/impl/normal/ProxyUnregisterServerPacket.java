package de.pocketcloud.cloudbridge.network.packet.impl.normal;

import de.pocketcloud.cloudbridge.network.packet.CloudPacket;
import de.pocketcloud.cloudbridge.network.packet.content.PacketContent;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ProxyUnregisterServerPacket extends CloudPacket {

    private String serverName;

    public ProxyUnregisterServerPacket(String serverName) {
        this.serverName = serverName;
    }

    @Override
    protected void encodePayload(PacketContent content) {
        super.encodePayload(content);
        content.put(serverName);
    }

    @Override
    protected void decodePayload(PacketContent content) {
        super.decodePayload(content);
        serverName = content.readString();
    }

    public String getServerName() {
        return serverName;
    }
}