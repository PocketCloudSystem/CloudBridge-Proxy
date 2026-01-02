package de.pocketcloud.cloud.bridge.network.packet.data;

import de.pocketcloud.cloud.bridge.network.packet.util.PacketData;

public enum TextType implements PacketData.Writable {

    MESSAGE,
    POPUP,
    TIP,
    TITLE,
    ACTION_BAR,
    TOAST_NOTIFICATION;
    
    public String getName() {
        return name();
    }

    @Override
    public String write() {
        return name();
    }

    public static TextType fromName(String name) {
        if (name == null) return null;
        try {
            return valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}