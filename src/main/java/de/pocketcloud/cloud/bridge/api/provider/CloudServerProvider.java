package de.pocketcloud.cloud.bridge.api.provider;

import de.pocketcloud.cloud.bridge.api.CloudAPI;
import de.pocketcloud.cloud.bridge.api.object.server.CloudServer;
import de.pocketcloud.cloud.bridge.util.CloudEnvironmentConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class CloudServerProvider implements CloudAPIProvider {
    
    private final Map<String, CloudServer> servers = new HashMap<>();

    public void add(CloudServer server) {
        if (isset(server)) {
            servers.get(server.getName()).sync(server.write());
        } else {
            servers.put(server.getName(), server);
        }
    }

    public void remove(CloudServer server) {
        if (isset(server)) {
            servers.remove(server.getName());
        }
    }

    public boolean isset(CloudServer server) {
        return isset(server.getName());
    }
    
    public boolean isset(String name) {
        return servers.containsKey(name);
    }

    public CloudServer get(String name) {
        return servers.get(name);
    }

    public CloudServer current() {
        CloudServer server = get(CloudEnvironmentConfig.getServerName());
        if (server == null) throw new RuntimeException("The return value of current() should not be null, wait for CloudAPI to index");
        return server;
    }

    public List<CloudServer> lobbyServers() {
        return servers.values().stream().filter(server -> server.getTemplate().isLobby()).collect(Collectors.toList());
    }

    public Map<String, CloudServer> getAll() {
        return new HashMap<>(servers);
    }

    public static CloudServerProvider provider() {
        return CloudAPI.get().getProvider(CloudServerProvider.class);
    }
}