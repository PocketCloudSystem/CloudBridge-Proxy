package de.pocketcloud.cloud.bridge.language;

import java.util.Collections;
import java.util.Map;

public record Language(String name, Map<String, String> messages) {

    private static Language current = null;

    public static void sync(String currentLanguage, Map<String, String> messages) {
        current = new Language(currentLanguage, messages);
    }

    public static Language current() {
        if (current == null) return new Language("Unknown", Collections.emptyMap());
        return current;
    }

    public Language(String name, Map<String, String> messages) {
        this.name = name;
        this.messages = Map.copyOf(messages);
    }

    public String translate(String message) {
        return translate(message, Map.of());
    }

    public String translate(String key, Map<String, Object> args) {
        String message = messages.getOrDefault(key, key);
        message = message.replace("{PREFIX}", messages.getOrDefault("inGame.prefix", ""));

        for (Map.Entry<String, Object> entry : args.entrySet()) {
            message = message.replace("%" + entry.getKey() + "%", entry.getValue().toString());
        }

        return message;
    }
}