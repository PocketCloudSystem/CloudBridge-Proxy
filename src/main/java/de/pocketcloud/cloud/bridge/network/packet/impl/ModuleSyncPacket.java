package de.pocketcloud.cloud.bridge.network.packet.impl;

import de.pocketcloud.cloud.bridge.api.cache.InGameModuleCache;
import de.pocketcloud.cloud.bridge.network.packet.ClientboundPacket;
import de.pocketcloud.cloud.bridge.network.packet.CloudPacket;
import de.pocketcloud.cloud.bridge.network.packet.util.PacketData;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@NoArgsConstructor
public final class ModuleSyncPacket extends CloudPacket implements ClientboundPacket {
    
    private Map<String, Boolean> data;

    public ModuleSyncPacket(Map<String, Boolean> data) {
        this.data = data;
    }
    
    @Override
    public void handle() {
        for (Map.Entry<String, Boolean> entry : data.entrySet()) {
            InGameModuleCache.setModuleState(entry.getKey(), entry.getValue());
        }
    }
    
    @Override
    public void encodePayload(PacketData packetData) {}
    
    @Override
    public void decodePayload(PacketData packetData) {
        data = new HashMap<>();
        for (Map.Entry<String, Object> entry : packetData.readMap().entrySet()) {
            if (entry.getValue() instanceof Boolean bool) {
                data.put(entry.getKey(), bool);
            }
        }
    }

    public static ModuleSyncPacket create(Map<String, Boolean> data) {
        return new ModuleSyncPacket(data);
    }
}