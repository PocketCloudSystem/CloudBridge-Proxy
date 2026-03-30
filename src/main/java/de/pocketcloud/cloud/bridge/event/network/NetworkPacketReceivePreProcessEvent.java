package de.pocketcloud.cloud.bridge.event.network;

import de.pocketcloud.cloud.bridge.network.Network;
import de.pocketcloud.cloud.bridge.network.util.Address;
import dev.waterdog.waterdogpe.event.CancellableEvent;
import lombok.Getter;

@Getter
public class NetworkPacketReceivePreProcessEvent extends NetworkEvent implements CancellableEvent {

    private final Address sender;
    private final String buffer;
    private final boolean encryption;

    public NetworkPacketReceivePreProcessEvent(Network network, Address sender, String buffer, boolean encryption) {
        super(network);
        this.sender = sender;
        this.buffer = buffer;
        this.encryption = encryption;
    }
}