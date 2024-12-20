package de.pocketcloud.cloudbridge.network.packet.impl.normal;

import de.pocketcloud.cloudbridge.network.packet.CloudPacket;
import de.pocketcloud.cloudbridge.network.packet.data.PacketData;
import dev.waterdog.waterdogpe.ProxyServer;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ProxyUnregisterServerPacket extends CloudPacket {

    private String serverName;

    public ProxyUnregisterServerPacket(String serverName) {
        this.serverName = serverName;
    }

    @Override
    protected void encodePayload(PacketData packetData) {
        super.encodePayload(packetData);
        packetData.write(serverName);
    }

    @Override
    protected void decodePayload(PacketData packetData) {
        super.decodePayload(packetData);
        serverName = packetData.readString();
    }

    @Override
    public void handle() {
        ProxyServer.getInstance().removeServerInfo(serverName);
    }

    public String getServerName() {
        return serverName;
    }
}