package de.pocketcloud.cloud.bridge.event.network;

import de.pocketcloud.cloud.bridge.network.Network;
import de.pocketcloud.cloud.bridge.network.packet.CloudboundPacket;
import dev.waterdog.waterdogpe.event.CancellableEvent;
import lombok.Getter;

@Getter
public class NetworkPacketPreSendEvent extends NetworkEvent implements CancellableEvent {

    protected CloudboundPacket packet;

    public NetworkPacketPreSendEvent(Network network, CloudboundPacket packet) {
        super(network);
        this.packet = packet;
    }
}