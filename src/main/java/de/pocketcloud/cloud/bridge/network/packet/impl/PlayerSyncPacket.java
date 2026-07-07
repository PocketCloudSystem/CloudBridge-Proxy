package de.pocketcloud.cloud.bridge.network.packet.impl;

import de.pocketcloud.cloud.bridge.api.object.player.CloudPlayer;
import de.pocketcloud.cloud.bridge.api.provider.CloudPlayerProvider;
import de.pocketcloud.network.packet.ClientboundPacket;
import de.pocketcloud.cloud.bridge.network.packet.CloudPacket;
import de.pocketcloud.network.packet.data.PacketData;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public final class PlayerSyncPacket extends CloudPacket implements ClientboundPacket {
    
    private CloudPlayer player;
    private boolean removal;
    
    public PlayerSyncPacket(CloudPlayer player, boolean removal) {
        this.player = player;
        this.removal = removal;
    }
    
    @Override
    public void handle() {
        if (removal) {
            CloudPlayerProvider.provider().remove(player);
        } else {
            CloudPlayerProvider.provider().add(player);
        }
    }

    @Override
    public void encodePayload(PacketData packetData) {}

    @Override
    public void decodePayload(PacketData packetData) {
        player = CloudPlayer.read(packetData.readMap());
        removal = Boolean.TRUE.equals(packetData.readBool());
    }
    
    public static PlayerSyncPacket create(CloudPlayer player, boolean removal) {
        return new PlayerSyncPacket(player, removal);
    }
}