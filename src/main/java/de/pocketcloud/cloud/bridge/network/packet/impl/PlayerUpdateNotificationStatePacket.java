package de.pocketcloud.cloud.bridge.network.packet.impl;

import de.pocketcloud.cloud.bridge.network.packet.CloudPacket;
import de.pocketcloud.network.packet.CloudboundPacket;
import de.pocketcloud.network.packet.data.PacketData;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public final class PlayerUpdateNotificationStatePacket extends CloudPacket implements CloudboundPacket {

    private String player;
    private boolean value;

    public PlayerUpdateNotificationStatePacket(String player, boolean value) {
        this.player = player;
        this.value = value;
    }

    @Override
    public void encodePayload(PacketData packetData) {
        packetData.writeAll(this.player, this.value);
    }

    @Override
    public void decodePayload(PacketData packetData) {}

    @Override
    public void handle() {}

    public static PlayerUpdateNotificationStatePacket create(String player, boolean value) {
        return new PlayerUpdateNotificationStatePacket(player, value);
    }
}