package de.pocketcloud.cloud.bridge.network.packet.impl.request;

import de.pocketcloud.cloud.bridge.network.packet.RequestPacket;
import de.pocketcloud.network.packet.data.PacketData;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
final public class ServerSaveRequestPacket extends RequestPacket {

    private String server;

    public ServerSaveRequestPacket(String server) {
        this.server = server;
    }

    @Override
    public void encodePayload(PacketData packetData) {
        packetData.writeAll(server);
    }

    public static ServerSaveRequestPacket create(String server) {
        return new ServerSaveRequestPacket(server);
    }
}