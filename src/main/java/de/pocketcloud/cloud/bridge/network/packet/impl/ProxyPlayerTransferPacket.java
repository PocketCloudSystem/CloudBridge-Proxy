package de.pocketcloud.cloud.bridge.network.packet.impl;

import de.pocketcloud.cloud.bridge.network.packet.ClientboundPacket;
import de.pocketcloud.cloud.bridge.network.packet.CloudPacket;
import de.pocketcloud.cloud.bridge.network.packet.util.PacketData;
import dev.waterdog.waterdogpe.ProxyServer;
import dev.waterdog.waterdogpe.network.serverinfo.ServerInfo;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public final class ProxyPlayerTransferPacket extends CloudPacket implements ClientboundPacket {

    private String player;
    private String server;

    public ProxyPlayerTransferPacket(String player, String server) {
        this.player = player;
        this.server = server;
    }

    @Override
    public void handle() {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(this.player);
        ServerInfo info = ProxyServer.getInstance().getServerInfo(this.server);
        if (player != null && info != null) {
            player.connect(info);
        }
    }

    @Override
    public void decodePayload(PacketData packetData) {
        player = packetData.readString();
        server = packetData.readString();
    }

    public static ProxyPlayerTransferPacket create(String player, String server) {
        return new ProxyPlayerTransferPacket(player, server);
    }
}
