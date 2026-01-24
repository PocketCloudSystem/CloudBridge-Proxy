package de.pocketcloud.cloud.bridge.network.packet.impl;

import de.pocketcloud.cloud.bridge.network.packet.ClientboundPacket;
import de.pocketcloud.cloud.bridge.network.packet.CloudPacket;
import de.pocketcloud.cloud.bridge.network.packet.util.PacketData;
import dev.waterdog.waterdogpe.ProxyServer;
import dev.waterdog.waterdogpe.network.serverinfo.BedrockServerInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.net.InetSocketAddress;

@Getter
@NoArgsConstructor
public final class ProxyRegisterServerPacket extends CloudPacket implements ClientboundPacket {

    private String serverName;
    private int port;

    public ProxyRegisterServerPacket(String serverName, int port) {
        this.serverName = serverName;
        this.port = port;
    }

    @Override
    public void handle() {
        ProxyServer.getInstance().registerServerInfo(new BedrockServerInfo(serverName, new InetSocketAddress(port), null));
    }

    @Override
    public void decodePayload(PacketData packetData) {
        this.serverName = packetData.readString();
        this.port = packetData.readInt();
    }

    public static ProxyRegisterServerPacket create(String serverName, int port) {
        return new ProxyRegisterServerPacket(serverName, port);
    }
}
