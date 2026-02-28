package de.pocketcloud.cloud.bridge.network.packet;

import de.pocketcloud.cloud.bridge.network.packet.util.PacketData;

/**
 * The normal response packet sent to sub-servers from the cloud after the sub-servers sent a request via RequestPacket
 * @see RequestPacket
 */
public abstract class ResponseClientPacket extends CloudPacket implements CloudboundPacket {

    private String requestId = "";

    @Override
    final public void encode(PacketData packetData) {
        super.encode(packetData);
        packetData.write(requestId);
    }

    @Override
    final public void decode(PacketData packetData) {
        super.decode(packetData);
        requestId = packetData.readString();
    }

    public ResponseClientPacket setRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    @Override
    final public void handle() {}
}