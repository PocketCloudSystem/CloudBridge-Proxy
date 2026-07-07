package de.pocketcloud.cloud.bridge.network.packet.type;

import de.pocketcloud.cloud.bridge.util.Writable;
import org.apache.logging.log4j.Level;

public enum LogType implements Writable<String> {

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
}