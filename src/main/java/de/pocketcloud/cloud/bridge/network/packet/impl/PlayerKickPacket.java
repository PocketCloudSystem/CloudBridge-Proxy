package de.pocketcloud.cloud.bridge.network.packet.impl;

import de.pocketcloud.cloud.bridge.network.packet.ClientboundPacket;
import de.pocketcloud.cloud.bridge.network.packet.CloudPacket;
import de.pocketcloud.cloud.bridge.network.packet.CloudboundPacket;
import de.pocketcloud.cloud.bridge.network.packet.util.PacketData;
import dev.waterdog.waterdogpe.ProxyServer;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public final class PlayerKickPacket extends CloudPacket implements ClientboundPacket, CloudboundPacket {

    private String player;
    private String reason;
    private String disconnectScreenMessage;

    public PlayerKickPacket(String player, String reason, String disconnectScreenMessage) {
        this.player = player;
        this.reason = reason;
        this.disconnectScreenMessage = disconnectScreenMessage;
    }

    @Override
    public void encodePayload(PacketData packetData) {
        packetData.writeAll(this.player, this.reason, this.disconnectScreenMessage);
    }

    @Override
    public void decodePayload(PacketData packetData) {
        this.player = packetData.readString();
        this.reason = packetData.readString();
        this.disconnectScreenMessage = packetData.readString();
    }

    @Override
    public void handle() {
        ProxiedPlayer proxiedPlayer;
        if ((proxiedPlayer = ProxyServer.getInstance().getPlayer(this.player)) != null) {
            String disconnectScreenMessage = this.disconnectScreenMessage.isBlank() ? reason : this.disconnectScreenMessage;
            proxiedPlayer.disconnect((CharSequence) disconnectScreenMessage);
        }
    }

    public static PlayerKickPacket create(String player, String reason, String disconnectScreenMessage) {
        return new PlayerKickPacket(player, reason, disconnectScreenMessage);
    }
}