package de.pocketcloud.cloud.bridge.network.packet.data;

import de.pocketcloud.cloud.bridge.network.packet.util.PacketData;

public enum VerifyStatus implements PacketData.Writable {

    DENIED,
    VERIFIED,
    NOT_APPLIED;
    
    public String getName() {
        return name();
    }

    @Override
    public String write() {
        return name();
    }

    public static VerifyStatus fromName(String name) {
        if (name == null) return null;
        try {
            return valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}