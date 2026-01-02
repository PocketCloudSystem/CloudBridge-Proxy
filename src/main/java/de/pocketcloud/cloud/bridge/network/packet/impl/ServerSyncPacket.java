package de.pocketcloud.cloud.bridge.network.packet.impl;

import de.pocketcloud.cloud.bridge.api.object.server.CloudServer;
import de.pocketcloud.cloud.bridge.api.provider.CloudServerProvider;
import de.pocketcloud.cloud.bridge.network.packet.ClientboundPacket;
import de.pocketcloud.cloud.bridge.network.packet.CloudPacket;
import de.pocketcloud.cloud.bridge.network.packet.util.PacketData;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public final class ServerSyncPacket extends CloudPacket implements ClientboundPacket {
    
    private CloudServer server;
    private boolean removal;

    public ServerSyncPacket(CloudServer server, boolean removal) {
        this.server = server;
        this.removal = removal;
    }
    
    @Override
    public void handle() {
        if (removal) {
            CloudServerProvider.provider().remove(server);
        } else {
            CloudServerProvider.provider().add(server);
        }
    }
    
    @Override
    public void encodePayload(PacketData packetData) {}
    
    @Override
    public void decodePayload(PacketData packetData) {
        server = packetData.readServer();
        removal = Boolean.TRUE.equals(packetData.readBool());
    }

    public static ServerSyncPacket create(CloudServer server, boolean removal) {
        return new ServerSyncPacket(server, removal);
    }
}