package de.pocketcloud.cloud.bridge.event.network;

import de.pocketcloud.cloud.bridge.network.Network;
import de.pocketcloud.cloud.bridge.network.packet.CloudboundPacket;
import lombok.Getter;

@Getter
public class NetworkPacketSentEvent extends NetworkEvent {

    protected final CloudboundPacket packet;
    protected final boolean success;

    public NetworkPacketSentEvent(Network network, CloudboundPacket packet, boolean success) {
        super(network);
        this.packet = packet;
        this.success = success;
    }
}