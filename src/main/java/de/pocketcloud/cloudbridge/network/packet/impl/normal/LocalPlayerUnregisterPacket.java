package de.pocketcloud.cloudbridge.network.packet.impl.normal;

import de.pocketcloud.cloudbridge.network.packet.CloudPacket;
import de.pocketcloud.cloudbridge.network.packet.content.PacketContent;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class LocalPlayerUnregisterPacket extends CloudPacket {

    private String player;

    public LocalPlayerUnregisterPacket(String player) {
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
