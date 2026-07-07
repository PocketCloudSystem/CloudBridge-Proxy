package de.pocketcloud.cloud.bridge.network.packet.type;

import de.pocketcloud.cloud.bridge.util.Writable;

public enum ServerErrorReason implements Writable<String> {

    NONE,
    TEMPLATE_EXISTENCE,
    MAX_SERVERS,
    SERVER_EXISTENCE,
    REQUEST_TIMEOUT;
    
    public String getName() {
        return name();
    }

    @Override
    public String write() {
        return name();
    }
}