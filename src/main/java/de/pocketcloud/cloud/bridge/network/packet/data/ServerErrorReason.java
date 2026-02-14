package de.pocketcloud.cloud.bridge.network.packet.data;

import de.pocketcloud.cloud.bridge.network.packet.util.PacketData;

public enum ServerErrorReason implements PacketData.Writable {

    NO_ERROR,
    TEMPLATE_EXISTENCE,
    MAX_SERVERS,
    SERVER_EXISTENCE;
    
    public String getName() {
        return name();
    }

    @Override
    public String write() {
        return name();
    }

    public static ServerErrorReason fromName(String name) {
        if (name == null) return null;
        try {
            return valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}