package de.pocketcloud.cloudbridge.network.packet.impl.normal;

import de.pocketcloud.cloudbridge.api.CloudAPI;
import de.pocketcloud.cloudbridge.api.player.CloudPlayer;
import de.pocketcloud.cloudbridge.api.registry.Registry;
import de.pocketcloud.cloudbridge.network.packet.CloudPacket;
import de.pocketcloud.cloudbridge.network.packet.utils.PacketData;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PlayerSyncPacket extends CloudPacket {

    private CloudPlayer player;
    private boolean removal;

    public PlayerSyncPacket(CloudPlayer player, boolean removal) {
        this.player = player;
        this.removal = removal;
    }

    @Override
    protected void encodePayload(PacketData packetData) {
        super.encodePayload(packetData);
        packetData.writePlayer(player);
        packetData.write(removal);
    }

    @Override
    protected void decodePayload(PacketData packetData) {
        super.decodePayload(packetData);
        player = packetData.readPlayer();
        removal = packetData.readBool();
    }

    public CloudPlayer getPlayer() {
        return player;
    }

    public boolean isRemoval() {
        return removal;
    }

    @Override
    public void handle() {
        if (CloudAPI.getInstance().getPlayerByName(player.getName()) == null) {
            if (!removal) Registry.registerPlayer(player);
        } else {
            if (removal) {
                Registry.unregisterPlayer(player.getName());
            } else if (player.getCurrentServer() != null) {
                Registry.updatePlayer(player.getName(), player.getCurrentServer().getName());
            }
        }
    }
}
