package de.pocketcloud.cloudbridge.network.packet.impl.normal;

import de.pocketcloud.cloudbridge.api.player.CloudPlayer;
import de.pocketcloud.cloudbridge.network.packet.CloudPacket;
import de.pocketcloud.cloudbridge.network.packet.content.PacketContent;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PlayerConnectPacket extends CloudPacket {

    private CloudPlayer player;

    public PlayerConnectPacket(CloudPlayer player) {
        this.player = player;
    }

    @Override
    protected void encodePayload(PacketContent content) {
        super.encodePayload(content);
        content.putPlayer(player);
    }

    @Override
    protected void decodePayload(PacketContent content) {
        super.decodePayload(content);
        player = content.readPlayer();
    }

    public CloudPlayer getPlayer() {
        return player;
    }
}
