package de.pocketcloud.cloud.bridge.network.packet;

import de.pocketcloud.cloud.bridge.network.packet.util.PacketData;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class ResponsePacket extends CloudPacket implements ClientboundPacket {
    
    private String requestId = "";
    
    @Override
    public void encode(PacketData packetData) {
        super.encode(packetData);
        packetData.write(requestId);
    }
    
    @Override
    public void decode(PacketData packetData) {
        super.decode(packetData);
        requestId = packetData.readString();
    }
}