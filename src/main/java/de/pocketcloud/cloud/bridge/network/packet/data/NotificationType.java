package de.pocketcloud.cloud.bridge.network.packet.data;

import de.pocketcloud.cloud.bridge.network.packet.util.PacketData;

public enum NotificationType implements PacketData.Writable {

    SERVER_STARTING,
    SERVER_STOPPING,
    SERVER_TIMED_OUT,
    SERVER_STOP_TIMED_OUT,
    SERVER_CRASHED,
    SERVER_START_FAILED,
    PLAYER_JOINED,
    PLAYER_LEFT,
    PLAYER_JOIN_FAILED,
    PLAYER_KICKED;
    
    public String getName() {
        return name();
    }

    @Override
    public String write() {
        return name();
    }

    public static NotificationType fromName(String name) {
        if (name == null) return null;
        try {
            return valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}