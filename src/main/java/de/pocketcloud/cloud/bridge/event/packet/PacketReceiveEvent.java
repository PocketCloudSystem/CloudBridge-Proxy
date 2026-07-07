package de.pocketcloud.cloud.bridge.event.packet;

import de.pocketcloud.network.packet.ClientboundPacket;
import dev.waterdog.waterdogpe.event.CancellableEvent;
import lombok.Getter;

@Getter
public class PacketReceiveEvent extends PacketEvent implements CancellableEvent {

    public PacketReceiveEvent(ClientboundPacket packet) {
        super(packet);
    }
}