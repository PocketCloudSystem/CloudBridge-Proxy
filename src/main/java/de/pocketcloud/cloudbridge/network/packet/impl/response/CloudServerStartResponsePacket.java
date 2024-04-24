package de.pocketcloud.cloudbridge.network.packet.impl.response;

import de.pocketcloud.cloudbridge.network.packet.ResponsePacket;
import de.pocketcloud.cloudbridge.network.packet.utils.PacketData;
import de.pocketcloud.cloudbridge.network.packet.impl.types.ErrorReason;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CloudServerStartResponsePacket extends ResponsePacket {

    private ErrorReason errorReason;

    public CloudServerStartResponsePacket(ErrorReason errorReason) {
        this.errorReason = errorReason;
    }

    @Override
    protected void encodePayload(PacketData packetData) {
        super.encodePayload(packetData);
        packetData.writeErrorReason(errorReason);
    }

    @Override
    protected void decodePayload(PacketData packetData) {
        super.decodePayload(packetData);
        errorReason = packetData.readErrorReason();
    }

    @Override
    public void handle() {}
}
