package de.pocketcloud.cloud.bridge.network.packet.data;

import de.pocketcloud.cloud.bridge.network.packet.util.PacketData;

public enum LogType implements PacketData.Writable {

    INFO,
    WARN,
    ERROR,
    SUCCESS,
    DEBUG;
    
    public String getName() {
        return name();
    }

    @Override
    public String write() {
        return name();
    }
    
    public static LogType fromName(String name) {
        if (name == null) return null;
        try {
            return valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}