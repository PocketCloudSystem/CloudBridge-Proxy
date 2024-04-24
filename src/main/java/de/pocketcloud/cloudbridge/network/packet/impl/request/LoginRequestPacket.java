package de.pocketcloud.cloudbridge.network.packet.impl.request;

import de.pocketcloud.cloudbridge.network.packet.RequestPacket;
import de.pocketcloud.cloudbridge.network.packet.utils.PacketData;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginRequestPacket extends RequestPacket {

    private String serverName;
    private int processId;
    private int maxPlayerCount;

    public LoginRequestPacket(String serverName, int processId, int maxPlayerCount) {
        this.serverName = serverName;
        this.processId = processId;
        this.maxPlayerCount = maxPlayerCount;
    }

    @Override
    protected void encodePayload(PacketData packetData) {
        super.encodePayload(packetData);
        packetData.write(serverName);
        packetData.write(processId);
        packetData.write(maxPlayerCount);
    }

    @Override
    protected void decodePayload(PacketData packetData) {
        super.decodePayload(packetData);
        serverName = packetData.readString();
        processId = packetData.readInt();
        maxPlayerCount = packetData.readInt();
    }
}
