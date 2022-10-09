package de.pocketcloud.cloudbridge.network.packet.impl.response;

import de.pocketcloud.cloudbridge.network.packet.ResponsePacket;
import de.pocketcloud.cloudbridge.network.packet.content.PacketContent;
import de.pocketcloud.cloudbridge.network.packet.impl.types.ErrorReason;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CloudServerStopResponsePacket extends ResponsePacket {

    private ErrorReason errorReason;

    public CloudServerStopResponsePacket(String requestId, ErrorReason errorReason) {
        super(requestId);
        this.errorReason = errorReason;
    }

    @Override
    protected void encodePayload(PacketContent content) {
        super.encodePayload(content);
        content.putErrorReason(errorReason);
    }

    @Override
    protected void decodePayload(PacketContent content) {
        super.decodePayload(content);
        errorReason = content.readErrorReason();
    }

    public ErrorReason getErrorReason() {
        return errorReason;
    }
}
