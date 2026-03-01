package de.pocketcloud.cloud.bridge.network.packet.impl.request;

import de.pocketcloud.cloud.bridge.network.packet.RequestPacket;
import de.pocketcloud.cloud.bridge.network.packet.util.PacketData;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
final public class ServerStopRequestPacket extends RequestPacket {

    private String server;
    private boolean forcefully;

    public ServerStopRequestPacket(String server, boolean forcefully) {
        this.server = server;
        this.forcefully = forcefully;
    }

    @Override
    public void encodePayload(PacketData packetData) {
        packetData.writeAll(server, forcefully);
    }

    public static ServerStopRequestPacket create(String server, boolean forcefully) {
        return new ServerStopRequestPacket(server, forcefully);
    }
}
