package de.pocketcloud.cloudbridge.api.template;

import de.pocketcloud.cloudbridge.util.Utils;
import java.util.Map;

public class Template {

    private String name;
    private boolean lobby;
    private boolean maintenance;
    private boolean staticServers;
    private int maxPlayerCount;
    private int minServerCount;
    private int maxServerCount;
    private boolean startNewWhenFull;
    private boolean autoStart;
    private String templateType;

    public Template(String name, boolean lobby, boolean maintenance, boolean staticServers, int maxPlayerCount, int minServerCount, int maxServerCount, boolean startNewWhenFull, boolean autoStart, String templateType) {
        this.name = name;
        this.lobby = lobby;
        this.maintenance = maintenance;
        this.staticServers = staticServers;
        this.maxPlayerCount = maxPlayerCount;
        this.minServerCount = minServerCount;
        this.maxServerCount = maxServerCount;
        this.startNewWhenFull = startNewWhenFull;
        this.autoStart = autoStart;
        this.templateType = templateType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isLobby() {
        return lobby;
    }

    public void setLobby(boolean lobby) {
        this.lobby = lobby;
    }

    public boolean isMaintenance() {
        return maintenance;
    }

    public void setMaintenance(boolean maintenance) {
        this.maintenance = maintenance;
    }

    public boolean isStatic() {
        return staticServers;
    }

    public void setStatic(boolean staticServers) {
        this.staticServers = staticServers;
    }

    public int getMaxPlayerCount() {
        return maxPlayerCount;
    }

    public void setMaxPlayerCount(int maxPlayerCount) {
        this.maxPlayerCount = maxPlayerCount;
    }

    public int getMinServerCount() {
        return minServerCount;
    }

    public void setMinServerCount(int minServerCount) {
        this.minServerCount = minServerCount;
    }

    public int getMaxServerCount() {
        return maxServerCount;
    }

    public void setMaxServerCount(int maxServerCount) {
        this.maxServerCount = maxServerCount;
    }

    public boolean isStartNewWhenFull() {
        return startNewWhenFull;
    }

    public void setStartNewWhenFull(boolean startNewWhenFull) {
        this.startNewWhenFull = startNewWhenFull;
    }

    public boolean isAutoStart() {
        return autoStart;
    }

    public void setAutoStart(boolean autoStart) {
        this.autoStart = autoStart;
    }

    public String getTemplateType() {
        return templateType;
    }

    public void apply(Map<?,?> data) {
        name = (String) data.get("name");
        lobby = (Boolean) data.get("lobby");
        maintenance = (Boolean) data.get("maintenance");
        staticServers = (Boolean) data.get("static");
        maxPlayerCount = ((Number) data.get("maxPlayerCount")).intValue();
        minServerCount = ((Number) data.get("minServerCount")).intValue();
        maxServerCount = ((Number) data.get("maxServerCount")).intValue();
        startNewWhenFull = (Boolean) data.get("startNewWhenFull");
        autoStart = (Boolean) data.get("autoStart");
        templateType = (String) data.get("templateType");
    }

    public Map<String, Object> toArray() {
        return Map.of(
                "name", name,
                "lobby", lobby,
                "maintenance", maintenance,
                "static", staticServers,
                "maxPlayerCount", maxPlayerCount,
                "minServerCount", minServerCount,
                "maxServerCount", maxServerCount,
                "startNewWhenFull", startNewWhenFull,
                "autoStart", autoStart,
                "templateType", templateType
        );
    }

    public static Template fromArray(Map<?,?> map) {
        if (Utils.containKeys(map, "name", "lobby", "maintenance", "static", "maxPlayerCount", "minServerCount", "maxServerCount", "startNewWhenFull", "autoStart", "templateType")) return null;
        return new Template(
                (String) map.get("name"),
                (Boolean) map.get("lobby"),
                (Boolean) map.get("maintenance"),
                (Boolean) map.get("static"),
                ((Number) map.get("maxPlayerCount")).intValue(),
                ((Number) map.get("minServerCount")).intValue(),
                ((Number) map.get("maxServerCount")).intValue(),
                (Boolean) map.get("startNewWhenFull"),
                (Boolean) map.get("autoStart"),
                (String) map.get("templateType")
        );
    }
}
