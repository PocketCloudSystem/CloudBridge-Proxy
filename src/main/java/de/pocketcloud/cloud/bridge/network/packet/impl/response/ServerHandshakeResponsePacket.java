package de.pocketcloud.cloud.bridge.network.packet.impl.response;

import de.pocketcloud.cloud.bridge.network.packet.ResponsePacket;
import de.pocketcloud.cloud.bridge.network.packet.data.VerifyStatus;
import de.pocketcloud.cloud.bridge.network.packet.util.PacketData;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public final class ServerHandshakeResponsePacket extends ResponsePacket {
    
    private VerifyStatus verifyStatus;

    public ServerHandshakeResponsePacket(VerifyStatus verifyStatus) {
        this.verifyStatus = verifyStatus;
    }
    
    @Override
    public void encodePayload(PacketData packetData) {}
    
    @Override
    public void decodePayload(PacketData packetData) {
        verifyStatus = packetData.readVerifyStatus();
    }
    
    @Override
    public void handle() {}

    public static ServerHandshakeResponsePacket create(VerifyStatus verifyStatus) {
        return new ServerHandshakeResponsePacket(verifyStatus);
    }
}