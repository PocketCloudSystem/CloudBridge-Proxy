package de.pocketcloud.cloud.bridge.network.packet;

import de.pocketcloud.cloud.bridge.network.packet.util.PacketData;

/**
 * Base interface for all packets in the network system.
 */
public interface Packet {

    void encode(PacketData packetData);

    void encodePayload(PacketData packetData);

    void decode(PacketData packetData);

    void decodePayload(PacketData packetData);

    void handle();

    String getName();

    boolean isEncoded();

    Double getSentTimestamp();
}