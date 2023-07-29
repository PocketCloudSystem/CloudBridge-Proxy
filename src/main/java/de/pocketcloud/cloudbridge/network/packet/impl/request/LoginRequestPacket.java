package de.pocketcloud.cloudbridge.network.packet.impl.request;

import de.pocketcloud.cloudbridge.network.packet.RequestPacket;
import de.pocketcloud.cloudbridge.network.packet.utils.PacketData;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class LoginRequestPacket extends RequestPacket {

    private String serverName;
    private int processId;

    public LoginRequestPacket(String serverName, int processId) {
        this.serverName = serverName;
        this.processId = processId;
    }

    @Override
    protected void encodePayload(PacketData packetData) {
        super.encodePayload(packetData);
        packetData.write(serverName);
        packetData.write(processId);
    }

    @Override
    protected void decodePayload(PacketData packetData) {
        super.decodePayload(packetData);
        serverName = packetData.readString();
        processId = packetData.readInt();
    }

    public String getServerName() {
        return serverName;
    }

    public int getProcessId() {
        return processId;
    }
}
