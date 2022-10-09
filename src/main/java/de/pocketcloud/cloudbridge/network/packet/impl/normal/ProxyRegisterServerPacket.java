package de.pocketcloud.cloudbridge.network.packet.impl.normal;

import de.pocketcloud.cloudbridge.network.packet.CloudPacket;
import de.pocketcloud.cloudbridge.network.packet.content.PacketContent;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ProxyRegisterServerPacket extends CloudPacket {

    private String serverName;
    private int port;

    public ProxyRegisterServerPacket(String serverName, int port) {
        this.serverName = serverName;
        this.port = port;
    }

    @Override
    protected void encodePayload(PacketContent content) {
        super.encodePayload(content);
        content.put(serverName);
        content.put(port);
    }

    @Override
    protected void decodePayload(PacketContent content) {
        super.decodePayload(content);
        serverName = content.readString();
        port = ((Number) content.readDouble()).intValue();
    }

    public String getServerName() {
        return serverName;
    }

    public int getPort() {
        return port;
    }
}
