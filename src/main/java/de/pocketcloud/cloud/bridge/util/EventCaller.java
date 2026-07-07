package de.pocketcloud.cloud.bridge.util;

import dev.waterdog.waterdogpe.ProxyServer;
import dev.waterdog.waterdogpe.event.Event;

public final class EventCaller {

    public static <T extends Event> T call(T event) {
        ProxyServer.getInstance().getEventManager().callEvent(event);
        return event;
    }
}