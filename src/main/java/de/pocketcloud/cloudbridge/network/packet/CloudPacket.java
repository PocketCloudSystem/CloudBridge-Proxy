package de.pocketcloud.cloudbridge.network.packet;

import de.pocketcloud.cloudbridge.network.packet.content.PacketContent;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public abstract class CloudPacket {

    public void encode(PacketContent content) {
        content.put(getIdentifier());
        encodePayload(content);
    }

    public void decode(PacketContent content) {
        content.read();
        decodePayload(content);
    }

    protected void encodePayload(PacketContent content) {}

    protected void decodePayload(PacketContent content) {}

    public String getIdentifier() {
        return getClass().getSimpleName();
    }
}
