package de.pocketcloud.cloud.bridge.network.packet.data;

import de.pocketcloud.cloud.bridge.network.packet.util.PacketData;

public enum ServerDisconnectReason implements PacketData.Writable {

    CLOUD_SHUTDOWN,
    SERVER_SHUTDOWN;
    
    public String getName() {
        return name();
    }

    @Override
    public String write() {
        return name();
    }

    public static ServerDisconnectReason fromName(String name) {
        if (name == null) return null;
        try {
            return valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}