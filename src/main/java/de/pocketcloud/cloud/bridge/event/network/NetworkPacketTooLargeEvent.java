package de.pocketcloud.cloud.bridge.event.network;

import de.pocketcloud.cloud.bridge.network.Network;
import de.pocketcloud.cloud.bridge.network.packet.CloudboundPacket;
import lombok.Getter;

@Getter
public class NetworkPacketTooLargeEvent extends NetworkEvent {

    protected CloudboundPacket packet;
    protected final int size;
    protected final String buffer;

    public NetworkPacketTooLargeEvent(Network network, CloudboundPacket packet, int size, String buffer) {
        super(network);
        this.packet = packet;
        this.size = size;
        this.buffer = buffer;
    }
}