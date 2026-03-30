package de.pocketcloud.cloud.bridge.network.packet.impl;

import de.pocketcloud.cloud.bridge.api.object.group.ServerGroup;
import de.pocketcloud.cloud.bridge.api.provider.ServerGroupProvider;
import de.pocketcloud.cloud.bridge.network.packet.ClientboundPacket;
import de.pocketcloud.cloud.bridge.network.packet.CloudPacket;
import de.pocketcloud.cloud.bridge.network.packet.util.PacketData;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public final class ServerGroupSyncPacket extends CloudPacket implements ClientboundPacket {
    
    private ServerGroup group;
    private boolean removal;

    public ServerGroupSyncPacket(ServerGroup group, boolean removal) {
        this.group = group;
        this.removal = removal;
    }
    
    @Override
    public void handle() {
        if (removal) {
            ServerGroupProvider.provider().remove(group);
        } else {
            ServerGroupProvider.provider().add(group);
        }
    }

    @Override
    public void decodePayload(PacketData packetData) {
        group = packetData.readServerGroup();
        removal = Boolean.TRUE.equals(packetData.readBool());
    }

    public static ServerGroupSyncPacket create(ServerGroup group, boolean removal) {
        return new ServerGroupSyncPacket(group, removal);
    }
}