package de.pocketcloud.cloud.bridge.event.network;

import de.pocketcloud.cloud.bridge.network.Network;
import dev.waterdog.waterdogpe.event.CancellableEvent;
import lombok.Getter;

@Getter
public class NetworkPacketReceivePreProcessEvent extends NetworkEvent implements CancellableEvent {

    protected final String buffer;
    protected final boolean encryption;

    public NetworkPacketReceivePreProcessEvent(Network network, String buffer, boolean encryption) {
        super(network);
        this.buffer = buffer;
        this.encryption = encryption;
    }
}