package de.pocketcloud.cloud.bridge.event.network;

import de.pocketcloud.cloud.bridge.network.Network;
import de.pocketcloud.cloud.bridge.network.packet.CloudboundPacket;
import de.pocketcloud.cloud.bridge.network.util.Address;
import dev.waterdog.waterdogpe.event.CancellableEvent;

public class NetworkPacketPreSendEvent extends NetworkPacketEvent implements CancellableEvent {

    public NetworkPacketPreSendEvent(Network network, Address sender, CloudboundPacket packet) {
        super(network, sender, packet);
    }

    @Override
    public CloudboundPacket getPacket() {
        return (CloudboundPacket) packet;
    }
}