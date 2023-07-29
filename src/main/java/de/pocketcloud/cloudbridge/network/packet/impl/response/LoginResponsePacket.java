package de.pocketcloud.cloudbridge.network.packet.impl.response;

import de.pocketcloud.cloudbridge.network.packet.ResponsePacket;
import de.pocketcloud.cloudbridge.network.packet.utils.PacketData;
import de.pocketcloud.cloudbridge.network.packet.impl.types.VerifyStatus;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class LoginResponsePacket extends ResponsePacket {

    private VerifyStatus verifyStatus;

    public LoginResponsePacket(VerifyStatus verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    @Override
    protected void encodePayload(PacketData packetData) {
        super.encodePayload(packetData);
        packetData.writeVerifyStatus(verifyStatus);
    }

    @Override
    protected void decodePayload(PacketData packetData) {
        super.decodePayload(packetData);
        verifyStatus = packetData.readVerifyStatus();
    }

    public VerifyStatus getVerifyStatus() {
        return verifyStatus;
    }

    @Override
    public void handle() {}
}
