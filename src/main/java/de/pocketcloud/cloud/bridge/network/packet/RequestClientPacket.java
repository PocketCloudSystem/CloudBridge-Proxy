package de.pocketcloud.cloud.bridge.network.packet;

import de.pocketcloud.cloud.bridge.network.Network;
import de.pocketcloud.cloud.bridge.network.packet.util.PacketData;
import lombok.Getter;

/**
 * A different version from the regular RequestClientPacket
 * This logic is reversed, means the cloud sends this RequestClientPacket and the sub-servers answer via ResponseClientPacket
 * @see RequestClientPacket
 * @see ResponseClientPacket
 */
@Getter
public abstract class RequestClientPacket extends CloudPacket implements ClientboundPacket {

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
    final public void encodePayload(PacketData packetData) {}

    public boolean sendResponse(ResponseClientPacket packet) {
        return Network.getInstance().sendPacket(packet.setRequestId(requestId));
    }
}
