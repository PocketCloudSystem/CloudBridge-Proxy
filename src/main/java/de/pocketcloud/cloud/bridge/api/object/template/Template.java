package de.pocketcloud.cloud.bridge.api.object.template;

import de.pocketcloud.cloud.bridge.api.object.group.ServerGroup;
import de.pocketcloud.cloud.bridge.api.provider.ServerGroupProvider;
import de.pocketcloud.cloud.bridge.network.packet.util.PacketData;
import de.pocketcloud.cloud.bridge.util.Utils;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public final class Template implements PacketData.Writable {

    @Getter
    private final String name;
    @Getter
    private boolean lobby;
    @Getter
    private boolean maintenance;
    private boolean staticServer;
    @Getter
    private boolean alwaysCopyToStaticServers;
    @Getter
    private int maxPlayerCount;
    @Getter
    private int minServerCount;
    @Getter
    private int maxServerCount;
    @Getter
    private float startNewPercentage;
    @Getter
    private boolean autoStart;
    @Getter
    private final String templateType;
    
    public Template(String name, boolean lobby, boolean maintenance, boolean staticServer,
                   boolean alwaysCopyToStaticServers, int maxPlayerCount, int minServerCount,
                   int maxServerCount, float startNewPercentage, boolean autoStart, String templateType) {
        this.name = name;
        this.lobby = lobby;
        this.maintenance = maintenance;
        this.staticServer = staticServer;
        this.alwaysCopyToStaticServers = alwaysCopyToStaticServers;
        this.maxPlayerCount = maxPlayerCount;
        this.minServerCount = minServerCount;
        this.maxServerCount = maxServerCount;
        this.startNewPercentage = startNewPercentage;
        this.autoStart = autoStart;
        this.templateType = templateType;
    }

    public void sync(Map<String, Object> data) {
        if (data.containsKey("lobby")) this.lobby = (Boolean) data.get("lobby");
        if (data.containsKey("maintenance")) this.maintenance = (Boolean) data.get("maintenance");
        if (data.containsKey("static")) this.staticServer = (Boolean) data.get("static");
        if (data.containsKey("alwaysCopyToStaticServers")) 
            this.alwaysCopyToStaticServers = (Boolean) data.get("alwaysCopyToStaticServers");
        if (data.containsKey("maxPlayerCount")) 
            this.maxPlayerCount = ((Number) data.get("maxPlayerCount")).intValue();
        if (data.containsKey("minServerCount")) 
            this.minServerCount = ((Number) data.get("minServerCount")).intValue();
        if (data.containsKey("maxServerCount")) 
            this.maxServerCount = ((Number) data.get("maxServerCount")).intValue();
        if (data.containsKey("startNewPercentage")) 
            this.startNewPercentage = ((Number) data.get("startNewPercentage")).floatValue();
        if (data.containsKey("autoStart")) this.autoStart = (Boolean) data.get("autoStart");
    }

    public boolean isStatic() {
        return staticServer;
    }

    public ServerGroup getParentServerGroup() {
        return ServerGroupProvider.provider().get(this);
    }
    
    @Override
    public Map<String, Object> write() {
        Map<String, Object> data = new HashMap<>();
        data.put("name", name);
        data.put("lobby", lobby);
        data.put("maintenance", maintenance);
        data.put("static", staticServer);
        data.put("alwaysCopyToStaticServers", alwaysCopyToStaticServers);
        data.put("maxPlayerCount", maxPlayerCount);
        data.put("minServerCount", minServerCount);
        data.put("maxServerCount", maxServerCount);
        data.put("startNewPercentage", startNewPercentage);
        data.put("autoStart", autoStart);
        data.put("templateType", templateType);
        return data;
    }
    
    public static Template read(Map<String, Object> data) {
        if (!Utils.containKeys(data, "name", "lobby", "maintenance", "static", "alwaysCopyToStaticServers",
                "maxPlayerCount", "minServerCount", "maxServerCount", "startNewPercentage",
                "autoStart", "templateType")) return null;

        try {
            return new Template(
                (String) data.get("name"),
                (Boolean) data.get("lobby"),
                (Boolean) data.get("maintenance"),
                (Boolean) data.get("static"),
                (Boolean) data.get("alwaysCopyToStaticServers"),
                ((Number) data.get("maxPlayerCount")).intValue(),
                ((Number) data.get("minServerCount")).intValue(),
                ((Number) data.get("maxServerCount")).intValue(),
                ((Number) data.get("startNewPercentage")).floatValue(),
                (Boolean) data.get("autoStart"),
                (String) data.get("templateType")
            );
        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }
}
