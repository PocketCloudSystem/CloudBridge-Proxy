package de.pocketcloud.cloud.bridge.event.network;

import de.pocketcloud.cloud.bridge.network.Network;

public class NetworkCloseEvent extends NetworkEvent {

    public NetworkCloseEvent(Network network) {
        super(network);
    }
}