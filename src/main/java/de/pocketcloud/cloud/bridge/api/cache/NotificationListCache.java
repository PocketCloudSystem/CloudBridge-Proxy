package de.pocketcloud.cloud.bridge.api.cache;

import java.util.ArrayList;
import java.util.List;

public class NotificationListCache {

    private static final ArrayList<String> notificationList = new ArrayList<>();

    public static void sync(List<String> data) {
        notificationList.clear();
        notificationList.addAll(data);
    }

    public static void add(String player) {
        notificationList.add(player);
    }

    public static void remove(String player) {
        notificationList.remove(player);
    }

    public static boolean is(String player) {
        return notificationList.stream().anyMatch(p -> p.equalsIgnoreCase(player));
    }

    public static ArrayList<String> getAll() {
        return notificationList;
    }
}