package de.pocketcloud.cloud.bridge.api.provider;

import de.pocketcloud.cloud.bridge.api.CloudAPI;
import de.pocketcloud.cloud.bridge.api.object.group.ServerGroup;
import de.pocketcloud.cloud.bridge.api.object.player.CloudPlayer;
import de.pocketcloud.cloud.bridge.api.object.server.CloudServer;
import de.pocketcloud.cloud.bridge.api.object.template.Template;
import de.pocketcloud.cloud.bridge.network.packet.RequestPacket;
import de.pocketcloud.cloud.bridge.network.packet.impl.request.ServerSaveRequestPacket;
import de.pocketcloud.cloud.bridge.network.packet.impl.request.ServerStartRequestPacket;
import de.pocketcloud.cloud.bridge.network.packet.impl.request.ServerStopRequestPacket;
import de.pocketcloud.cloud.bridge.util.CloudEnvironmentConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class CloudServerProvider implements CloudAPIProvider {
    
    private final Map<String, CloudServer> servers = new HashMap<>();

    public RequestPacket start(Template template) {
        return start(template.getName(), 1);
    }

    public RequestPacket start(String template) {
        return start(template, 1);
    }

    public RequestPacket start(Template template, int count) {
        return start(template.getName(), count);
    }

    public RequestPacket start(String template, int count) {
        if (count < 0) count = 1;
        return ServerStartRequestPacket.create(template, count).sendRequest();
    }

    public RequestPacket stop(Template template) {
        return stop(template.getName(), false);
    }

    public RequestPacket stop(Template template, boolean forcefully) {
        return stop(template.getName(), forcefully);
    }

    public RequestPacket stop(CloudServer server) {
        return stop(server.getName(), false);
    }

    public RequestPacket stop(CloudServer server, boolean forcefully) {
        return stop(server.getName(), forcefully);
    }

    public RequestPacket stop(ServerGroup group) {
        return stop(group.getName(), false);
    }

    public RequestPacket stop(ServerGroup group, boolean forcefully) {
        return stop(group.getName(), forcefully);
    }

    public RequestPacket stop(String server) {
        return stop(server, false);
    }

    public RequestPacket stop(String server, boolean forcefully) {
        return ServerStopRequestPacket.create(server, forcefully).sendRequest();
    }

    public RequestPacket save(CloudServer server) {
        return ServerSaveRequestPacket.create(server.getName()).sendRequest();
    }

    public RequestPacket save(String server) {
        return ServerSaveRequestPacket.create(server).sendRequest();
    }

    public void add(CloudServer server) {
        if (isset(server)) {
            servers.get(server.getName()).sync(server.write());
        } else {
            servers.put(server.getName(), server);
        }
    }

    public void addAll(List<CloudServer> servers) {
        for (CloudServer server : servers) {
            add(server);
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