package de.pocketcloud.cloud.bridge.api.object.server.util;

import lombok.Getter;

@Getter
public enum ServerStatus {

    PENDING("§gPENDING"),
    STARTING("§2STARTING"),
    ONLINE("§aONLINE"),
    FULL("§eFULL"),
    IN_GAME("§6INGAME"),
    STOPPING("§4STOPPING"),
    OFFLINE("§cOFFLINE");
    
    private final String display;
    
    ServerStatus(String display) {
        this.display = display;
    }
    
    public String getName() {
        return name();
    }

    public static ServerStatus fromName(String name) {
        if (name == null) return null;
        try {
            return valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
