package de.pocketcloud.cloudbridge.network.packet;

import de.pocketcloud.cloudbridge.network.packet.utils.PacketData;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public abstract class ResponsePacket extends CloudPacket {

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

    public String getRequestId() {
        return requestId;
    }
}