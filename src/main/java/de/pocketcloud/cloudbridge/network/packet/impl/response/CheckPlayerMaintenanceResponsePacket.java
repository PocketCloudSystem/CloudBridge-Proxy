package de.pocketcloud.cloudbridge.network.packet.impl.response;

import de.pocketcloud.cloudbridge.network.packet.ResponsePacket;
import de.pocketcloud.cloudbridge.network.packet.utils.PacketData;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CheckPlayerMaintenanceResponsePacket extends ResponsePacket {

    private boolean value;

    public CheckPlayerMaintenanceResponsePacket(boolean value) {
        this.value = value;
    }

    @Override
    protected void encodePayload(PacketData packetData) {
        super.encodePayload(packetData);
        packetData.write(value);
    }

    @Override
    protected void decodePayload(PacketData packetData) {
        super.decodePayload(packetData);
        value = packetData.readBool();
    }

    @Override
    public void handle() {}

    public boolean isValue() {
        return value;
    }
}
