package de.pocketcloud.cloud.bridge.network.packet.impl;

import de.pocketcloud.cloud.bridge.api.cache.MaintenanceListCache;
import de.pocketcloud.network.packet.ClientboundPacket;
import de.pocketcloud.cloud.bridge.network.packet.CloudPacket;
import de.pocketcloud.network.packet.data.PacketData;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public final class MaintenanceListSyncPacket extends CloudPacket implements ClientboundPacket {

    private List<String> maintenanceList;

    public MaintenanceListSyncPacket(List<String> maintenanceList) {
        this.maintenanceList = maintenanceList;
    }

    @Override
    public void encodePayload(PacketData packetData) {}

    @Override
    public void decodePayload(PacketData packetData) {
        maintenanceList = new ArrayList<>();
        for (Object o : packetData.readArray()) {
            maintenanceList.add((String) o);
        }
    }

    @Override
    public void handle() {
        MaintenanceListCache.sync(maintenanceList);
    }

    public static MaintenanceListSyncPacket create(List<String> data) {
        return new MaintenanceListSyncPacket(data);
    }
}