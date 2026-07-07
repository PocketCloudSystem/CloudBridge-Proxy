package de.pocketcloud.cloud.bridge.network.packet.impl;

import de.pocketcloud.cloud.bridge.api.cache.NotificationListCache;
import de.pocketcloud.network.packet.ClientboundPacket;
import de.pocketcloud.cloud.bridge.network.packet.CloudPacket;
import de.pocketcloud.network.packet.data.PacketData;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public final class NotificationListSyncPacket extends CloudPacket implements ClientboundPacket {

    private List<String> notificationList;

    public NotificationListSyncPacket(List<String> notificationList) {
        this.notificationList = notificationList;
    }

    @Override
    public void encodePayload(PacketData packetData) {}

    @Override
    public void decodePayload(PacketData packetData) {
        notificationList = new ArrayList<>();
        for (Object o : packetData.readArray()) {
            notificationList.add((String) o);
        }
    }

    @Override
    public void handle() {
        NotificationListCache.sync(notificationList);
    }

    public static NotificationListSyncPacket create(List<String> data) {
        return new NotificationListSyncPacket(data);
    }
}