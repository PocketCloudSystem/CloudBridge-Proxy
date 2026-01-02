package de.pocketcloud.cloud.bridge.network.packet;

import de.pocketcloud.cloud.bridge.network.packet.util.PacketData;

/**
 * Marker interface for packets that are sent from the Cloud to the Client (Server).
 * ClientboundPacket -> Server (Client) is the receiver, Cloud is the sender
 */
public interface ClientboundPacket extends Packet {

    @Override
    void encodePayload(PacketData packetData);
}