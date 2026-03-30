package de.pocketcloud.cloud.bridge.event.network;

import de.pocketcloud.cloud.bridge.network.Network;
import de.pocketcloud.cloud.bridge.network.packet.ClientboundPacket;
import de.pocketcloud.cloud.bridge.network.util.Address;
import dev.waterdog.waterdogpe.event.CancellableEvent;

public class NetworkPacketReceiveEvent extends NetworkPacketEvent implements CancellableEvent {

    public NetworkPacketReceiveEvent(Network network, Address sender, ClientboundPacket packet) {
        super(network, sender, packet);
    }

    @Override
    public ClientboundPacket getPacket() {
        return (ClientboundPacket) packet;
    }
}