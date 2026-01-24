package de.pocketcloud.cloud.bridge.api.cache;

import de.pocketcloud.cloud.bridge.network.packet.impl.PlayerUpdateNotificationStatePacket;

import java.util.ArrayList;
import java.util.List;

final public class NotificationListCache {

    private static final ArrayList<String> notificationList = new ArrayList<>();

    public static void sync(List<String> data) {
        notificationList.clear();
        notificationList.addAll(data);
    }

    public static void add(String player) {
        notificationList.add(player);
        PlayerUpdateNotificationStatePacket.create(player, true).sendPacket();
    }

    public static void remove(String player) {
        notificationList.remove(player);
        PlayerUpdateNotificationStatePacket.create(player, false).sendPacket();
    }

    public static boolean is(String player) {
        return notificationList.stream().anyMatch(p -> p.equalsIgnoreCase(player));
    }

    public static ArrayList<String> getAll() {
        return notificationList;
    }
}