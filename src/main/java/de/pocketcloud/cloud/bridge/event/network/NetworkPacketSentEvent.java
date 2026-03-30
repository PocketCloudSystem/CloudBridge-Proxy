package de.pocketcloud.cloud.bridge.event.network;

import de.pocketcloud.cloud.bridge.network.Network;
import de.pocketcloud.cloud.bridge.network.packet.CloudboundPacket;
import de.pocketcloud.cloud.bridge.network.util.Address;
import lombok.Getter;

@Getter
public class NetworkPacketSentEvent extends NetworkPacketEvent {

    private final boolean success;

    public NetworkPacketSentEvent(Network network, Address sender, CloudboundPacket packet, boolean success) {
        super(network, sender, packet);
        this.success = success;
    }

    @Override
    public CloudboundPacket getPacket() {
        return (CloudboundPacket) packet;
    }
}