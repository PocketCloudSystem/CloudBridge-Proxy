package de.pocketcloud.cloudbridge.network.packet;

import de.pocketcloud.cloudbridge.network.packet.utils.PacketData;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public abstract class CloudPacket {

    private boolean encoded = false;

    public void encode(PacketData packetData) {
        if (!encoded) {
            encoded = true;
            packetData.write(getClass().getSimpleName());
            encodePayload(packetData);
        }
    }

    public void decode(PacketData packetData) {
        packetData.readString();
        decodePayload(packetData);
    }

    protected void encodePayload(PacketData packetData) {}

    protected void decodePayload(PacketData packetData) {}

    abstract public void handle();
}
