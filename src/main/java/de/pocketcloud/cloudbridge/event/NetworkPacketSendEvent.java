package de.pocketcloud.cloudbridge.event;

import de.pocketcloud.cloudbridge.network.packet.CloudPacket;
import dev.waterdog.waterdogpe.event.CancellableEvent;
import dev.waterdog.waterdogpe.event.Event;

public class NetworkPacketSendEvent extends Event implements CancellableEvent {

    private final CloudPacket packet;

    public NetworkPacketSendEvent(CloudPacket packet) {
        this.packet = packet;
    }

    public CloudPacket getPacket() {
        return packet;
    }
}