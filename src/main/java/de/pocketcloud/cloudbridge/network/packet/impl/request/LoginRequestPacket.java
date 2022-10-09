package de.pocketcloud.cloudbridge.network.packet.impl.request;

import de.pocketcloud.cloudbridge.network.packet.RequestPacket;
import de.pocketcloud.cloudbridge.network.packet.content.PacketContent;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class LoginRequestPacket extends RequestPacket {

    private String serverName;
    private int processId;

    public LoginRequestPacket(String serverName, int processId) {
        super();
        this.serverName = serverName;
        this.processId = processId;
    }

    @Override
    protected void encodePayload(PacketContent content) {
        super.encodePayload(content);
        content.put(serverName);
        content.put(processId);
    }

    @Override
    protected void decodePayload(PacketContent content) {
        super.decodePayload(content);
        serverName = content.readString();
        processId = content.readInt();
    }

    public String getServerName() {
        return serverName;
    }

    public int getProcessId() {
        return processId;
    }
}
