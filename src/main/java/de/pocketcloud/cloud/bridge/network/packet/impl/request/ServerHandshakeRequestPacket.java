package de.pocketcloud.cloud.bridge.network.packet.impl.request;

import de.pocketcloud.cloud.bridge.network.packet.RequestPacket;
import de.pocketcloud.cloud.bridge.network.packet.util.PacketData;
import de.pocketcloud.cloud.bridge.network.request.RequestManager;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public final class ServerHandshakeRequestPacket extends RequestPacket {
    
    private String serverName;
    private Integer processId;
    private Integer maxPlayers;

    public ServerHandshakeRequestPacket(String serverName, Integer processId, Integer maxPlayers) {
        this.serverName = serverName;
        this.processId = processId;
        this.maxPlayers = maxPlayers;
    }
    
    @Override
    public void encodePayload(PacketData packetData) {
        packetData.writeAll(serverName, processId, maxPlayers);
    }
    
    @Override
    public void decodePayload(PacketData packetData) {}

    public static RequestPacket makeRequest(String serverName, int pid, int maxPlayers) {
        return RequestManager.getInstance().send(new ServerHandshakeRequestPacket(serverName, pid, maxPlayers));
    }
    
    public static ServerHandshakeRequestPacket create(String serverName, int processId, int maxPlayers) {
        return new ServerHandshakeRequestPacket(serverName, processId, maxPlayers);
    }
}