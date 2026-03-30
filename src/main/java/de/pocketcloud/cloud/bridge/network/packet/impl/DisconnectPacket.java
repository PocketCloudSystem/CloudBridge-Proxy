package de.pocketcloud.cloud.bridge.network.packet.impl;

import de.pocketcloud.cloud.bridge.CloudBridge;
import de.pocketcloud.cloud.bridge.network.packet.ClientboundPacket;
import de.pocketcloud.cloud.bridge.network.packet.CloudboundPacket;
import de.pocketcloud.cloud.bridge.network.packet.CloudPacket;
import de.pocketcloud.cloud.bridge.network.packet.data.ServerDisconnectReason;
import de.pocketcloud.cloud.bridge.network.packet.util.PacketData;
import dev.waterdog.waterdogpe.ProxyServer;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public final class DisconnectPacket extends CloudPacket implements ClientboundPacket, CloudboundPacket {
    
    private ServerDisconnectReason reason;
    
    public DisconnectPacket(ServerDisconnectReason reason) {
        this.reason = reason;
    }
    
    @Override
    public void handle() {
        if (reason == ServerDisconnectReason.CLOUD_SHUTDOWN) {
            CloudBridge.getInstance().getLogger().warn("§4Cloud was stopped, shutting down this instance...");
        } else {
            CloudBridge.getInstance().getLogger().warn("§4Server shutdown was ordered by the cloud, shutting down this instance...");
        }

        ProxyServer.getInstance().shutdown();
    }
    
    @Override
    public void encodePayload(PacketData packetData) {
        packetData.writeAll(reason);
    }
    
    @Override
    public void decodePayload(PacketData packetData) {
        reason = packetData.readServerDisconnectReason();
    }

    public static DisconnectPacket create(ServerDisconnectReason reason) {
        return new DisconnectPacket(reason);
    }
}