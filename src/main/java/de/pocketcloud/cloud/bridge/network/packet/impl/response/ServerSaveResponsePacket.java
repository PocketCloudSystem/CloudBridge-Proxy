package de.pocketcloud.cloud.bridge.network.packet.impl.response;

import de.pocketcloud.cloud.bridge.network.packet.ResponsePacket;
import de.pocketcloud.cloud.bridge.network.packet.data.ServerErrorReason;
import de.pocketcloud.cloud.bridge.network.packet.util.PacketData;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
final public class ServerSaveResponsePacket extends ResponsePacket {

    private ServerErrorReason errorReason;

    public ServerSaveResponsePacket(ServerErrorReason errorReason) {
        this.errorReason = errorReason;
    }

    @Override
    public void decodePayload(PacketData packetData) {
        errorReason = packetData.readServerErrorReason();
    }

    @Override
    public void handle() {}

    public static ServerSaveResponsePacket create(ServerErrorReason errorReason) {
        return new ServerSaveResponsePacket(errorReason);
    }
}
