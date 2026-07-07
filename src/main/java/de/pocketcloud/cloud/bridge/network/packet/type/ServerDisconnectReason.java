package de.pocketcloud.cloud.bridge.network.packet.type;

import de.pocketcloud.cloud.bridge.util.Writable;

public enum ServerDisconnectReason implements Writable<String> {

    CLOUD_SHUTDOWN,
    SERVER_SHUTDOWN;
    
    public String getName() {
        return name();
    }

    @Override
    public String write() {
        return name();
    }
}