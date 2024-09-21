package de.pocketcloud.cloudbridge.network.packet.impl.normal;

import de.pocketcloud.cloudbridge.network.packet.CloudPacket;
import de.pocketcloud.cloudbridge.network.packet.utils.PacketData;
import dev.waterdog.waterdogpe.ProxyServer;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PlayerKickPacket extends CloudPacket {

    private String player;
    private String reason;

    public PlayerKickPacket(String player, String reason) {
        this.player = player;
        this.reason = reason;
    }

    @Override
    protected void encodePayload(PacketData packetData) {
        super.encodePayload(packetData);
        packetData.write(player);
        packetData.write(reason);
    }

    @Override
    protected void decodePayload(PacketData packetData) {
        super.decodePayload(packetData);
        player = packetData.readString();
        reason = packetData.readString();
    }

    @Override
    public void handle() {
        ProxiedPlayer player;
        if ((player = ProxyServer.getInstance().getPlayer(this.player)) != null) player.disconnect(reason);
    }

    public String getPlayer() {
        return player;
    }

    public String getReason() {
        return reason;
    }
}
