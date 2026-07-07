package de.pocketcloud.cloud.bridge.network.packet;

import de.pocketcloud.network.packet.ClientboundPacket;
import de.pocketcloud.network.packet.data.PacketData;
import lombok.Getter;
import lombok.Setter;

/**
 * The normal response packet sent to sub-servers from the cloud after the sub-servers sent a request via RequestPacket
 * @see RequestPacket
 */
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

    @Override
    final public void encodePayload(PacketData packetData) {}
}