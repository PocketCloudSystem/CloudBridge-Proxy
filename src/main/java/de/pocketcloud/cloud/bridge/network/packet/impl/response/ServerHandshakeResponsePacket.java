package de.pocketcloud.cloud.bridge.network.packet.impl.response;

import de.pocketcloud.cloud.bridge.network.packet.ResponsePacket;
import de.pocketcloud.cloud.bridge.network.packet.type.VerificationStatus;
import de.pocketcloud.network.packet.data.PacketData;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public final class ServerHandshakeResponsePacket extends ResponsePacket {
    
    private VerificationStatus verifyStatus;

    public ServerHandshakeResponsePacket(VerificationStatus verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    @Override
    public void decodePayload(PacketData packetData) {
        verifyStatus = packetData.readEnum(VerificationStatus.class);
    }
    
    @Override
    public void handle() {}

    public static ServerHandshakeResponsePacket create(VerificationStatus verifyStatus) {
        return new ServerHandshakeResponsePacket(verifyStatus);
    }
}