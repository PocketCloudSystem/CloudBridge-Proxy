package de.pocketcloud.cloudbridge.network.packet.impl.normal;

import de.pocketcloud.cloudbridge.network.packet.CloudPacket;
import de.pocketcloud.cloudbridge.network.packet.content.PacketContent;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PlayerKickPacket extends CloudPacket {

    private String player;
    private String reason;

    public PlayerKickPacket(String player, String reason) {
        this.player = player;
        this.reason = reason;
    }

    @Override
    protected void encodePayload(PacketContent content) {
        super.encodePayload(content);
        content.put(player);
        content.put(reason);
    }

    @Override
    protected void decodePayload(PacketContent content) {
        super.decodePayload(content);
        player = content.readString();
        reason = content.readString();
    }

    public String getPlayer() {
        return player;
    }

    public String getReason() {
        return reason;
    }
}
