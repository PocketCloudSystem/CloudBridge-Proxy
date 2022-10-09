package de.pocketcloud.cloudbridge.network.packet.impl.types;

public enum DisconnectReason {

    CLOUD_SHUTDOWN("CLOUD_SHUTDOWN"),
    SERVER_SHUTDOWN("SERVER_SHUTDOWN");

    public static DisconnectReason getReasonByName(String name) {
        return valueOf(name);
    }

    private final String name;

    DisconnectReason(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
