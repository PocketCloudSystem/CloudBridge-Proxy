package de.pocketcloud.cloudbridge.network.packet.impl.types;

public enum ErrorReason {

    NO_ERROR("NO_ERROR"),
    TEMPLATE_EXISTENCE("TEMPLATE_EXISTENCE"),
    MAX_SERVERS("MAX_SERVERS"),
    SERVER_EXISTENCE("SERVER_EXISTENCE");

    public static ErrorReason getReasonByName(String name) {
        return valueOf(name);
    }

    private final String name;

    ErrorReason(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
