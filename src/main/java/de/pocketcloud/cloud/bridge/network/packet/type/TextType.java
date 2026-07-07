package de.pocketcloud.cloud.bridge.network.packet.type;

import de.pocketcloud.cloud.bridge.util.Writable;

public enum TextType implements Writable<String> {

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
}