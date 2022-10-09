package de.pocketcloud.cloudbridge.network.packet.impl.request;

import de.pocketcloud.cloudbridge.network.packet.RequestPacket;
import de.pocketcloud.cloudbridge.network.packet.content.PacketContent;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CheckPlayerMaintenanceRequestPacket extends RequestPacket {

    private String player;

    public CheckPlayerMaintenanceRequestPacket(String player) {
        super();
        this.player = player;
    }

    @Override
    protected void encodePayload(PacketContent content) {
        super.encodePayload(content);
        content.put(player);
    }

    @Override
    protected void decodePayload(PacketContent content) {
        super.decodePayload(content);
        player = content.readString();
    }

    public String getPlayer() {
        return player;
    }
}
