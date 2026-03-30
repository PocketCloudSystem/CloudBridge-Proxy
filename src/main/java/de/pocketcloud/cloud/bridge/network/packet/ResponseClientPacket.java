package de.pocketcloud.cloud.bridge.network.packet;

import de.pocketcloud.cloud.bridge.network.packet.util.PacketData;

/**
 * A different version from the regular ResponsePacket
 * This logic is reversed, means the sub-servers sends this ResponseClientPacket in response to the RequestClientPacket
 * @see RequestClientPacket
 * @see ResponseClientPacket
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

    @Override
    final public void decodePayload(PacketData packetData) {}

    public ResponseClientPacket setRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    @Override
    final public void handle() {}
}