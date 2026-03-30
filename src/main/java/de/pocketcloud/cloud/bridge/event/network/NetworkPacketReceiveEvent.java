package de.pocketcloud.cloud.bridge.event.network;

import de.pocketcloud.cloud.bridge.network.Network;
import de.pocketcloud.cloud.bridge.network.packet.ClientboundPacket;
import dev.waterdog.waterdogpe.event.CancellableEvent;
import lombok.Getter;

@Getter
public class NetworkPacketReceiveEvent extends NetworkEvent implements CancellableEvent {

    protected ClientboundPacket packet;

    public NetworkPacketReceiveEvent(Network network, ClientboundPacket packet) {
        super(network);
        this.packet = packet;
    }
}