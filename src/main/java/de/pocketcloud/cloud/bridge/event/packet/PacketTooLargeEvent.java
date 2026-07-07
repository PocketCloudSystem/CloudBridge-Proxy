package de.pocketcloud.cloud.bridge.event.packet;

import de.pocketcloud.network.packet.Packet;
import de.pocketcloud.network.traffic.TrafficDirection;
import lombok.Getter;

@Getter
public class PacketTooLargeEvent extends PacketEvent {

    protected final int size;
    protected final TrafficDirection direction;

    public PacketTooLargeEvent(Packet packet, int size, TrafficDirection direction) {
        super(packet);
        this.size = size;
        this.direction = direction;
    }
}