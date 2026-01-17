package de.pocketcloud.cloud.bridge.api.cache;

import java.util.ArrayList;
import java.util.List;

final public class MaintenanceListCache {

    private static final ArrayList<String> maintenanceList = new ArrayList<>();

    public static void sync(List<String> data) {
        maintenanceList.clear();
        maintenanceList.addAll(data);
    }
    
    public static void add(String player) {
        maintenanceList.add(player);
    }
    
    public static void remove(String player) {
        maintenanceList.remove(player);
    }
    
    public static boolean is(String player) {
        return maintenanceList.stream().anyMatch(p -> p.equalsIgnoreCase(player));
    }

    public static ArrayList<String> getAll() {
        return maintenanceList;
    }
}