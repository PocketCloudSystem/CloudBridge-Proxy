package de.pocketcloud.cloud.bridge.api.provider;

import de.pocketcloud.cloud.bridge.api.CloudAPI;
import de.pocketcloud.cloud.bridge.api.object.player.CloudPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CloudPlayerProvider implements CloudAPIProvider {

    private final Map<String, CloudPlayer> players = new HashMap<>();

    public void add(CloudPlayer player) {
        if (isset(player)) {
            players.get(player.getName()).sync(player.write());
        } else {
            players.put(player.getName(), player);
        }
    }

    public void remove(CloudPlayer player) {
        if (isset(player)) {
            players.remove(player.getName());
        }
    }

    public boolean isset(CloudPlayer player) {
        return isset(player.getName());
    }
    
    public boolean isset(String name) {
        return players.containsKey(name);
    }

    public CloudPlayer get(String name) {
        return players.get(name);
    }

    public List<CloudPlayer> getAll() {
        return new ArrayList<>(players.values());
    }

    public static CloudPlayerProvider provider() {
        return CloudAPI.get().getProvider(CloudPlayerProvider.class);
    }
}