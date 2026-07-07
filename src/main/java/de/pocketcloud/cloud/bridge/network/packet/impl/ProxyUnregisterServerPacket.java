package de.pocketcloud.cloud.bridge.network.packet.impl;

import de.pocketcloud.network.packet.ClientboundPacket;
import de.pocketcloud.cloud.bridge.network.packet.CloudPacket;
import de.pocketcloud.network.packet.data.PacketData;
import dev.waterdog.waterdogpe.ProxyServer;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public final class ProxyUnregisterServerPacket extends CloudPacket implements ClientboundPacket {

    private String serverName;

    public ProxyUnregisterServerPacket(String serverName) {
        this.serverName = serverName;
    }

    @Override
    public void handle() {
        ProxyServer.getInstance().removeServerInfo(serverName);
    }

    @Override
    public void encodePayload(PacketData packetData) {}

    @Override
    public void decodePayload(PacketData packetData) {
        this.serverName = packetData.readString();
    }

    public static ProxyUnregisterServerPacket create(String serverName) {
        return new ProxyUnregisterServerPacket(serverName);
    }
}