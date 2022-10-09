package de.pocketcloud.cloudbridge.network.packet.impl.response;

import de.pocketcloud.cloudbridge.network.packet.ResponsePacket;
import de.pocketcloud.cloudbridge.network.packet.content.PacketContent;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CheckPlayerMaintenanceResponsePacket extends ResponsePacket {

    private String player;
    private boolean value;

    public CheckPlayerMaintenanceResponsePacket(String requestId, String player, boolean value) {
        super(requestId);
        this.player = player;
        this.value = value;
    }

    @Override
    protected void encodePayload(PacketContent content) {
        super.encodePayload(content);
        content.put(player);
        content.put(value);
    }

    @Override
    protected void decodePayload(PacketContent content) {
        super.decodePayload(content);
        player = content.readString();
        value = content.readBool();
    }

    public String getPlayer() {
        return player;
    }

    public boolean getValue() {
        return value;
    }
}
