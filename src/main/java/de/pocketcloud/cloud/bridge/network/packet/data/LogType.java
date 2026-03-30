package de.pocketcloud.cloud.bridge.network.packet.data;

import de.pocketcloud.cloud.bridge.network.packet.util.PacketData;
import org.apache.logging.log4j.Level;

public enum LogType implements PacketData.Writable {

    INFO,
    WARN,
    ERROR,
    SUCCESS,
    DEBUG;
    
    public String getName() {
        return name();
    }

    public Level toLogLevel() {
        return switch (this) {
            case INFO, SUCCESS -> Level.INFO;
            case WARN -> Level.WARN;
            case ERROR -> Level.ERROR;
            case DEBUG -> Level.DEBUG;
        };
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