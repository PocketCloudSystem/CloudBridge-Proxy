package de.pocketcloud.cloud.bridge.event.packet;

import de.pocketcloud.network.packet.CloudboundPacket;
import lombok.Getter;

@Getter
public class PacketSentEvent extends PacketEvent {

    public PacketSentEvent(CloudboundPacket packet) {
        super(packet);
    }
}