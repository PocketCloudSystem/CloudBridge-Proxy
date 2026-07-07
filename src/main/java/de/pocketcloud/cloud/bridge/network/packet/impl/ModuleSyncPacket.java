package de.pocketcloud.cloud.bridge.network.packet.impl;

import de.pocketcloud.cloud.bridge.api.cache.InGameModuleCache;
import de.pocketcloud.network.packet.ClientboundPacket;
import de.pocketcloud.cloud.bridge.network.packet.CloudPacket;
import de.pocketcloud.network.packet.data.PacketData;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public final class ModuleSyncPacket extends CloudPacket implements ClientboundPacket {
    
    private List<String> data;

    public ModuleSyncPacket(List<String> data) {
        this.data = data;
    }
    
    @Override
    public void handle() {
        InGameModuleCache.sync(data);
    }
    
    @Override
    public void encodePayload(PacketData packetData) {}
    
    @Override
    public void decodePayload(PacketData packetData) {
        data = packetData.readArray(String.class);
    }

    public static ModuleSyncPacket create(List<String> data) {
        return new ModuleSyncPacket(data);
    }
}