package de.pocketcloud.cloud.bridge.network.packet.impl.response;

import de.pocketcloud.cloud.bridge.network.packet.ResponsePacket;
import de.pocketcloud.cloud.bridge.network.packet.type.ActionFailureReason;
import de.pocketcloud.cloud.bridge.network.packet.type.ServerErrorReason;
import de.pocketcloud.network.packet.data.PacketData;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
final public class ServerStopResponsePacket extends ResponsePacket {

    private ActionFailureReason errorReason;

    public ServerStopResponsePacket(ActionFailureReason errorReason) {
        this.errorReason = errorReason;
    }

    @Override
    public void decodePayload(PacketData packetData) {
        errorReason = packetData.readEnum(ActionFailureReason.class);
    }

    @Override
    public void handle() {}

    public static ServerStopResponsePacket create(ActionFailureReason errorReason) {
        return new ServerStopResponsePacket(errorReason);
    }
}