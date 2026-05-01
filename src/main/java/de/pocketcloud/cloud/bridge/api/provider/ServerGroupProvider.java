package de.pocketcloud.cloud.bridge.api.provider;

import de.pocketcloud.cloud.bridge.api.CloudAPI;
import de.pocketcloud.cloud.bridge.api.object.group.ServerGroup;
import de.pocketcloud.cloud.bridge.api.object.template.Template;
import de.pocketcloud.cloud.bridge.util.CloudEnvironmentConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ServerGroupProvider implements CloudAPIProvider {
    private final Map<String, ServerGroup> serverGroups = new HashMap<>();

    public void add(ServerGroup serverGroup) {
        if (isset(serverGroup)) {
            serverGroups.get(serverGroup.getName()).sync(serverGroup.write());
        } else {
            serverGroups.put(serverGroup.getName(), serverGroup);
        }
    }

    public void addAll(List<ServerGroup> serverGroups) {
        for (ServerGroup group : serverGroups) {
            add(group);
        }
    }

    public void remove(ServerGroup serverGroup) {
        if (isset(serverGroup)) {
            serverGroups.remove(serverGroup.getName());
        }
    }

    public boolean isset(ServerGroup serverGroup) {
        return isset(serverGroup.getName());
    }
    
    public boolean isset(String name) {
        return serverGroups.containsKey(name);
    }

    public ServerGroup get(String name) {
        if (serverGroups.containsKey(name)) return serverGroups.get(name);
        for (ServerGroup group : serverGroups.values()) {
            if (group.is(name)) {
                return group;
            }
        }
        
        return null;
    }
    
    public ServerGroup get(Template template) {
        return get(template.getName());
    }

    public ServerGroup current() {
        return get(CloudEnvironmentConfig.getTemplateName());
    }

    public Map<String, ServerGroup> getAll() {
        return new HashMap<>(serverGroups);
    }

    public static ServerGroupProvider provider() {
        return CloudAPI.get().getProvider(ServerGroupProvider.class);
    }
}