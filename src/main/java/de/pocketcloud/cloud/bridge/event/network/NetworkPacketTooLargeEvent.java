package de.pocketcloud.cloud.bridge.event.network;

import de.pocketcloud.cloud.bridge.network.Network;
import de.pocketcloud.cloud.bridge.network.packet.CloudboundPacket;
import de.pocketcloud.cloud.bridge.network.packet.Packet;
import de.pocketcloud.cloud.bridge.network.util.Address;
import lombok.Getter;

@Getter
public class NetworkPacketTooLargeEvent extends NetworkPacketEvent {

    private final int size;
    private final String buffer;

    public NetworkPacketTooLargeEvent(Network network, Address sender, Packet packet, int size, String buffer) {
        super(network, sender, packet);
        this.size = size;
        this.buffer = buffer;
    }

    @Override
    public CloudboundPacket getPacket() {
        return (CloudboundPacket) packet;
    }
}