package de.pocketcloud.cloudbridge.network.packet.impl.normal;

import de.pocketcloud.cloudbridge.network.packet.CloudPacket;
import de.pocketcloud.cloudbridge.network.packet.content.PacketContent;
import de.pocketcloud.cloudbridge.network.packet.impl.types.DisconnectReason;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DisconnectPacket extends CloudPacket {

    private DisconnectReason disconnectReason;

    public DisconnectPacket(DisconnectReason disconnectReason) {
        this.disconnectReason = disconnectReason;
    }

    @Override
    protected void encodePayload(PacketContent content) {
        super.encodePayload(content);
        content.putDisconnectReason(disconnectReason);
    }

    @Override
    protected void decodePayload(PacketContent content) {
        super.decodePayload(content);
        disconnectReason = content.readDisconnectReason();
    }

    public DisconnectReason getDisconnectReason() {
        return disconnectReason;
    }
}
