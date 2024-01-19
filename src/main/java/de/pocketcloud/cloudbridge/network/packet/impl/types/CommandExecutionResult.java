package de.pocketcloud.cloudbridge.network.packet.impl.types;

import de.pocketcloud.cloudbridge.util.Utils;

import java.util.ArrayList;
import java.util.Map;

public record CommandExecutionResult(String commandLine, ArrayList<String> messages) {

    public String getMessage(int index) {
        return messages.get(index);
    }

    public String getCommandLine() {
        return commandLine;
    }

    public ArrayList<String> getMessages() {
        return messages;
    }

    public Map<String, Object> toArray() {
        return Map.of(
                "command_line", commandLine,
                "messages", messages
        );
    }

    public static CommandExecutionResult fromArray(Map<?, ?> data) {
        if (!Utils.containKeys(data, "command_line", "messages")) return null;
        if (data.get("messages") instanceof ArrayList<?>) {
            return new CommandExecutionResult(
                    (String) data.get("command_line"),
                    (ArrayList<String>) data.get("messages")
            );
        }
        return null;
    }
}
