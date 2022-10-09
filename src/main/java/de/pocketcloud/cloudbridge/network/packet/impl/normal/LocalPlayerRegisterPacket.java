package de.pocketcloud.cloudbridge.network.packet.impl.normal;

import de.pocketcloud.cloudbridge.network.packet.CloudPacket;
import de.pocketcloud.cloudbridge.network.packet.content.PacketContent;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
public class LocalPlayerRegisterPacket extends CloudPacket {

    private Map<?,?> player;

    public LocalPlayerRegisterPacket(Map<?,?> player) {
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
        player = content.readMap();
    }

    public Map<?,?> getPlayer() {
        return player;
    }
}
