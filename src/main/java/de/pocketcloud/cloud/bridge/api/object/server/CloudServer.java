package de.pocketcloud.cloud.bridge.api.object.server;

import de.pocketcloud.cloud.bridge.api.object.player.CloudPlayer;
import de.pocketcloud.cloud.bridge.api.object.server.data.CloudServerData;
import de.pocketcloud.cloud.bridge.api.object.server.data.CloudServerStorage;
import de.pocketcloud.cloud.bridge.api.object.server.util.ServerStatus;
import de.pocketcloud.cloud.bridge.api.object.template.Template;
import de.pocketcloud.cloud.bridge.api.provider.CloudPlayerProvider;
import de.pocketcloud.cloud.bridge.api.provider.TemplateProvider;
import de.pocketcloud.cloud.bridge.network.packet.type.VerificationStatus;
import de.pocketcloud.cloud.bridge.network.packet.impl.ServerChangeStatusPacket;
import de.pocketcloud.cloud.bridge.util.Writable;
import de.pocketcloud.cloud.bridge.util.mapper.MapperUtils;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
public final class CloudServer implements Writable<Map<String, Object>> {

    private final int id;
    private final UUID uuid;
    private final String templateName;
    private final CloudServerData serverData;
    private ServerStatus status;
    private final CloudServerStorage serverStorage;
    private VerificationStatus verificationStatus = VerificationStatus.PENDING;
    private Long startTime = null;
    private Long verifiedTime = null;

    public CloudServer(int id, UUID uuid, String templateName, CloudServerData serverData, ServerStatus status, Map<String, Object> serverStorage) {
        this.id = id;
        this.uuid = uuid;
        this.templateName = templateName;
        this.serverData = serverData;
        this.status = status;
        this.serverStorage = new CloudServerStorage(this, serverStorage);
    }

    @SuppressWarnings("unchecked")
    public void sync(Map<String, Object> data) {
        if (data.containsKey("status")) {
            String statusName = (String) data.get("status");
            this.status = ServerStatus.fromName(statusName);
        }

        if (data.containsKey("storage")) {
            Map<String, Object> storage = (Map<String, Object>) data.get("storage");
            this.serverStorage.sync(storage);
        }

        if (data.containsKey("verificationStatus")) {
            String statusName = (String) data.get("verificationStatus");
            this.verificationStatus = VerificationStatus.fromName(statusName);
        }

        if (data.containsKey("startTime")) {
            this.startTime = (Long) data.get("startTime");
        }

        if (data.containsKey("verifiedTime")) {
            this.verifiedTime = (Long) data.get("verifiedTime");
        }
    }

    public void setServerStatus(ServerStatus status) {
        this.status = status;
        ServerChangeStatusPacket.create(this.uuid.toString(), status).sendPacket();
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
        return templateName + "-" + id;
    }

    public Template getTemplate() {
        return TemplateProvider.provider().get(templateName);
    }

    @Override
    public Map<String, Object> write() {
        return MapperUtils.toMap(this);
    }
    
    @SuppressWarnings("unchecked")
    public static CloudServer read(Map<String, Object> data) {
        return MapperUtils.fromMap(data, CloudServer.class);
    }
}