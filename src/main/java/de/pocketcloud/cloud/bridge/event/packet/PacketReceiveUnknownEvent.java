package de.pocketcloud.cloud.bridge.event.packet;

import de.pocketcloud.cloud.bridge.event.network.NetworkEvent;
import lombok.Getter;

@Getter
public class PacketReceiveUnknownEvent extends NetworkEvent {

    private final byte[] payload;
    private final int length;
    private final boolean encryption;

    public PacketReceiveUnknownEvent(byte[] payload, int length, boolean encryption) {
        this.payload = payload;
        this.length = length;
        this.encryption = encryption;
    }
}