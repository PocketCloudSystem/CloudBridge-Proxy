package de.pocketcloud.cloud.bridge.api.object.server;

import de.pocketcloud.cloud.bridge.CloudBridge;
import de.pocketcloud.cloud.bridge.api.object.player.CloudPlayer;
import de.pocketcloud.cloud.bridge.api.object.server.data.CloudServerData;
import de.pocketcloud.cloud.bridge.api.object.server.data.CloudServerStorage;
import de.pocketcloud.cloud.bridge.api.object.server.util.ServerStatus;
import de.pocketcloud.cloud.bridge.api.object.template.Template;
import de.pocketcloud.cloud.bridge.api.provider.CloudPlayerProvider;
import de.pocketcloud.cloud.bridge.api.provider.TemplateProvider;
import de.pocketcloud.cloud.bridge.network.packet.impl.ServerChangeStatusPacket;
import de.pocketcloud.cloud.bridge.network.packet.util.PacketData;
import de.pocketcloud.cloud.bridge.util.Utils;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class CloudServer implements PacketData.Writable {

    @Getter
    private final int id;
    @Getter
    private final String serverUuid;
    private final String template;
    @Getter
    private final CloudServerData serverData;
    @Getter
    private ServerStatus serverStatus;
    @Getter
    private CloudServerStorage serverStorage;
    
    public CloudServer(int id, String serverUuid, String template, CloudServerData serverData, ServerStatus serverStatus, Map<String, Object> serverStorage) {
        this.id = id;
        this.serverUuid = serverUuid;
        this.template = template;
        this.serverData = serverData;
        this.serverStatus = serverStatus;
        this.serverStorage = new CloudServerStorage(this, serverStorage);
    }
    

    @SuppressWarnings("unchecked")
    public void sync(Map<String, Object> data) {
        if (data.containsKey("serverStatus")) {
            String statusName = (String) data.get("serverStatus");
            this.serverStatus = ServerStatus.fromName(statusName);
        }
        if (data.containsKey("internalStorage")) {
            Map<String, Object> storage = (Map<String, Object>) data.get("internalStorage");
            this.serverStorage.sync(storage);
        }
    }

    public void setServerStatus(ServerStatus serverStatus) {
        this.serverStatus = serverStatus;
        ServerChangeStatusPacket.create(this.serverUuid, serverStatus).sendPacket();
    }

    public CloudPlayer getPlayer(String identifier) {
        return getPlayers().stream()
            .filter(player -> player.getName().equals(identifier) ||
                            player.getUniqueId().equals(identifier) ||
                            player.getXboxUserId().equals(identifier))
            .findFirst()
            .orElse(null);
    }

    public List<CloudPlayer> getPlayers() {
        String serverName = getName();
        return CloudPlayerProvider.provider().getAll().stream()
            .filter(player -> serverName.equals(player.getCurrentProxyName()) || serverName.equals(player.getCurrentServerName()))
            .collect(Collectors.toList());
    }

    public int getPlayerCount() {
        return getPlayers().size();
    }

    public String getName() {
        return template + "-" + id;
    }

    public Template getTemplate() {
        return TemplateProvider.provider().get(template);
    }
    
    public String getTemplateName() {
        return template;
    }

    @Override
    public Map<String, Object> write() {
        Map<String, Object> data = new HashMap<>();
        data.put("name", getName());
        data.put("uuid", serverUuid);
        data.put("id", id);
        data.put("template", template);
        data.put("port", serverData.port());
        data.put("maxPlayers", serverData.maxPlayers());
        data.put("processId", serverData.processId());
        data.put("serverStatus", serverStatus.getName());
        data.put("internalStorage", serverStorage.getAll());
        return data;
    }
    
    @SuppressWarnings("unchecked")
    public static CloudServer read(Map<String, Object> data) {
        if (!Utils.containKeys(data, "name", "uuid", "id", "template", "port", "maxPlayers", "serverStatus")) return null;

        try {
            int id = ((Number) data.get("id")).intValue();
            String uuid = (String) data.get("uuid");
            String template = (String) data.get("template");
            String name = (String) data.get("name");
            int port = ((Number) data.get("port")).intValue();
            int maxPlayers = ((Number) data.get("maxPlayers")).intValue();
            Integer processId = data.containsKey("processId") && data.get("proccessId") != null ?
                ((Number) data.get("processId")).intValue() : null;
            ServerStatus status = ServerStatus.fromName((String) data.get("serverStatus"));

            Map<String, Object> storage = Map.of();
            if (data.containsKey("internalStorage")) {
                if (data.get("internalStorage") instanceof Map) {
                    storage = (Map<String, Object>) data.get("internalStorage");
                }
            }

            CloudServerData serverData = new CloudServerData(name, port, maxPlayers, processId);
            
            return new CloudServer(id, uuid, template, serverData, status, storage);
        } catch (ClassCastException | NullPointerException e) {
            CloudBridge.getInstance().getLogger().error(e);
            return null;
        }
    }
}
