package de.pocketcloud.cloud.bridge.network.packet.impl;

import de.pocketcloud.cloud.bridge.network.packet.CloudPacket;
import de.pocketcloud.network.packet.CloudboundPacket;
import de.pocketcloud.network.packet.data.PacketData;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@NoArgsConstructor
final public class CloudSyncServerStoragePacket extends CloudPacket implements CloudboundPacket {

    private Map<String, Object> data;

    public CloudSyncServerStoragePacket(Map<String, Object> data) {
        this.data = data;
    }

    @Override
    public void encodePayload(PacketData packetData) {
        packetData.writeAll(data);
    }

    @Override
    public void decodePayload(PacketData packetData) {}

    @Override
    public void handle() {}

    public static CloudSyncServerStoragePacket create(Map<String, Object> data) {
        return new CloudSyncServerStoragePacket(data);
    }
}