package de.pocketcloud.cloud.bridge.network.packet.data;

import de.pocketcloud.cloud.bridge.language.LanguageKey;
import de.pocketcloud.cloud.bridge.network.packet.impl.CloudNotificationPacket;
import de.pocketcloud.cloud.bridge.network.packet.util.PacketData;

import java.util.Map;

public enum NotificationType implements PacketData.Writable {

    SERVER_STARTING,
    SERVER_STOPPING,
    SERVER_TIMED_OUT,
    SERVER_STOP_TIMED_OUT,
    SERVER_CRASHED,
    SERVER_START_FAILED,
    PLAYER_JOINED,
    PLAYER_LEFT,
    PLAYER_JOIN_FAILED,
    PLAYER_KICKED,
    PLAYER_SWITCHED_SERVER;

    public boolean sendNotification(Map<String, Object> args) {
        return CloudNotificationPacket.create(this, args).sendPacket();
    }

    public String getName() {
        return name();
    }

    @Override
    public String write() {
        return name();
    }

    public LanguageKey getLangKey() {
        return switch (this) {
            case SERVER_STARTING -> LanguageKey.INGAME_NOTIFY_MESSAGE_SERVER_STARTING;
            case SERVER_STOPPING -> LanguageKey.INGAME_NOTIFY_MESSAGE_SERVER_STOPPING;
            case SERVER_TIMED_OUT -> LanguageKey.INGAME_NOTIFY_MESSAGE_SERVER_TIMED_OUT;
            case SERVER_STOP_TIMED_OUT -> LanguageKey.INGAME_NOTIFY_MESSAGE_SERVER_STOP_TIMED_OUT;
            case SERVER_CRASHED -> LanguageKey.INGAME_NOTIFY_MESSAGE_SERVER_CRASHED;
            case SERVER_START_FAILED -> LanguageKey.INGAME_NOTIFY_MESSAGE_SERVER_START_FAILED;
            case PLAYER_JOINED -> LanguageKey.INGAME_NOTIFY_MESSAGE_PLAYER_JOINED;
            case PLAYER_LEFT -> LanguageKey.INGAME_NOTIFY_MESSAGE_PLAYER_LEFT;
            case PLAYER_JOIN_FAILED -> LanguageKey.INGAME_NOTIFY_MESSAGE_PLAYER_JOIN_FAILED;
            case PLAYER_SWITCHED_SERVER ->  LanguageKey.INGAME_NOTIFY_MESSAGE_PLAYER_SWITCHED_SERVER;
            default -> null;
        };
    }

    public static NotificationType fromName(String name) {
        if (name == null) return null;
        try {
            return valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}