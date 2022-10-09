package de.pocketcloud.cloudbridge.network.packet.impl.response;

import de.pocketcloud.cloudbridge.network.packet.ResponsePacket;
import de.pocketcloud.cloudbridge.network.packet.content.PacketContent;
import de.pocketcloud.cloudbridge.network.packet.impl.types.VerifyStatus;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class LoginResponsePacket extends ResponsePacket {

    private VerifyStatus verifyStatus;

    public LoginResponsePacket(String requestId, VerifyStatus verifyStatus) {
        super(requestId);
        this.verifyStatus = verifyStatus;
    }

    @Override
    protected void encodePayload(PacketContent content) {
        super.encodePayload(content);
        content.putVerifyStatus(verifyStatus);
    }

    @Override
    protected void decodePayload(PacketContent content) {
        super.decodePayload(content);
        verifyStatus = content.readVerifyStatus();
    }

    public VerifyStatus getVerifyStatus() {
        return verifyStatus;
    }
}
