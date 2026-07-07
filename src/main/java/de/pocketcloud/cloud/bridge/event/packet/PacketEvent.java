package de.pocketcloud.cloud.bridge.event.packet;

import de.pocketcloud.cloud.bridge.event.network.NetworkEvent;
import de.pocketcloud.network.packet.Packet;
import lombok.Getter;

public abstract class PacketEvent extends NetworkEvent {

    @Getter
    private final Packet packet;

    public PacketEvent(Packet packet) {
        this.packet = packet;
    }
}