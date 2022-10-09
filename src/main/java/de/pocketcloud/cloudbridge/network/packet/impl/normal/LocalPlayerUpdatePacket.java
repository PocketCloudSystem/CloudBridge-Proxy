package de.pocketcloud.cloudbridge.network.packet.impl.normal;

import de.pocketcloud.cloudbridge.network.packet.CloudPacket;
import de.pocketcloud.cloudbridge.network.packet.content.PacketContent;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class LocalPlayerUpdatePacket extends CloudPacket {

    private String player;
    private String newServer;

    public LocalPlayerUpdatePacket(String player, String newServer) {
        this.player = player;
        this.newServer = newServer;
    }

    @Override
    protected void encodePayload(PacketContent content) {
        super.encodePayload(content);
        content.put(player);
        content.put(newServer);
    }

    @Override
    protected void decodePayload(PacketContent content) {
        super.decodePayload(content);
        player = content.readString();
        newServer = content.readString();
    }

    public String getPlayer() {
        return player;
    }

    public String getNewServer() {
        return newServer;
    }
}
