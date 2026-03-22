package de.pocketcloud.cloud.bridge.event.network;

import de.pocketcloud.cloud.bridge.network.Network;
import dev.waterdog.waterdogpe.event.Event;
import lombok.Getter;

@Getter
public abstract class NetworkEvent extends Event {

    protected final Network network;

    public NetworkEvent(Network network) {
        this.network = network;
    }
}