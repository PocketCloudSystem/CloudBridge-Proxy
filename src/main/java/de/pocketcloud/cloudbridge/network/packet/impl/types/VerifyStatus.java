package de.pocketcloud.cloudbridge.network.packet.impl.types;

public enum VerifyStatus {

    NOT_VERIFIED("NOT_VERIFIED"),
    VERIFIED("VERIFIED");

    public static VerifyStatus getStatusByName(String name) {
        return valueOf(name);
    }

    private final String name;

    VerifyStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
