package de.pocketcloud.cloud.bridge.event.packet;

import de.pocketcloud.network.packet.CloudboundPacket;
import dev.waterdog.waterdogpe.event.CancellableEvent;
import lombok.Getter;

@Getter
public class PacketPreSendEvent extends PacketEvent implements CancellableEvent {

    public PacketPreSendEvent(CloudboundPacket packet) {
        super(packet);
    }
}