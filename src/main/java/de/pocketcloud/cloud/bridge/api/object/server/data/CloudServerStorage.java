package de.pocketcloud.cloud.bridge.api.object.server.data;

import de.pocketcloud.cloud.bridge.api.object.server.CloudServer;
import de.pocketcloud.cloud.bridge.network.packet.impl.CloudSyncServerStoragePacket;
import de.pocketcloud.cloud.bridge.util.CloudEnvironmentConfig;

import java.util.HashMap;
import java.util.Map;

public final class CloudServerStorage {

    private final CloudServer server;
    private Map<String, Object> storage;
    
    public CloudServerStorage(CloudServer server, Map<String, Object> storage) {
        this.server = server;
        this.storage = new HashMap<>(storage);
    }

    public void sync(Map<String, Object> data) {
        this.storage = new HashMap<>(data);
    }

    private void outgoingSync() {
        if (!server.getName().equals(CloudEnvironmentConfig.getServerName())) throw new RuntimeException("You are not allowed to edit other servers server storage");
        CloudSyncServerStoragePacket.create(storage).sendPacket();
    }

    public CloudServerStorage set(String key, Object value) {
        storage.put(key, value);
        outgoingSync();
        return this;
    }

    public CloudServerStorage remove(String key) {
        if (storage.containsKey(key)) {
            storage.remove(key);
            outgoingSync();
        }
        return this;
    }

    public boolean has(String key) {
        return storage.containsKey(key);
    }

    public Object get(String key, Object defaultValue) {
        return storage.getOrDefault(key, defaultValue);
    }
    
    public Object get(String key) {
        return get(key, null);
    }

    public CloudServerStorage clear() {
        storage.clear();
        outgoingSync();
        return this;
    }

    public boolean isEmpty() {
        return storage.isEmpty();
    }

    public CloudServer server() {
        return server;
    }

    public Map<String, Object> getAll() {
        return new HashMap<>(storage);
    }
}
