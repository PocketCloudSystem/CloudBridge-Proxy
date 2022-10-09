package de.pocketcloud.cloudbridge.network.packet;

import de.pocketcloud.cloudbridge.network.packet.content.PacketContent;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ResponsePacket extends CloudPacket {

    private String requestId;

    public ResponsePacket(String requestId) {
        this.requestId = requestId;
    }

    @Override
    public void encode(PacketContent content) {
        super.encode(content);
        content.put(requestId);
    }

    @Override
    public void decode(PacketContent content) {
        super.decode(content);
        requestId = content.readString();
    }

    public String getRequestId() {
        return requestId;
    }
}
