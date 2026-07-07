package de.pocketcloud.cloud.bridge.network.packet.type;

import de.pocketcloud.cloud.bridge.util.Writable;

public enum VerificationStatus implements Writable<String> {

    DENIED,
    VERIFIED,
    PENDING;
    
    public String getName() {
        return name();
    }

    @Override
    public String write() {
        return name();
    }

    public static VerificationStatus fromName(String name) {
        if (name == null) return null;
        try {
            return valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}