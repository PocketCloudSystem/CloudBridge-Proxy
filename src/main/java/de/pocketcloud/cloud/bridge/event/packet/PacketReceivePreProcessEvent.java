package de.pocketcloud.cloud.bridge.event.packet;

import de.pocketcloud.cloud.bridge.event.network.NetworkEvent;
import dev.waterdog.waterdogpe.event.CancellableEvent;
import lombok.Getter;

@Getter
public class PacketReceivePreProcessEvent extends NetworkEvent implements CancellableEvent {

    protected final byte[] payload;
    protected final boolean encryption;

    public PacketReceivePreProcessEvent(byte[] payload, boolean encryption) {
        this.payload = payload;
        this.encryption = encryption;
    }
}