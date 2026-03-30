package de.pocketcloud.cloud.bridge.event.network;

import de.pocketcloud.cloud.bridge.network.Network;
import de.pocketcloud.cloud.bridge.network.packet.Packet;
import de.pocketcloud.cloud.bridge.network.util.Address;
import lombok.Getter;

@Getter
public abstract class NetworkPacketEvent extends NetworkEvent {

    protected final Address sender;
    protected final Packet packet;

    public NetworkPacketEvent(Network network, Address sender, Packet packet) {
        super(network);
        this.sender = sender;
        this.packet = packet;
    }
}