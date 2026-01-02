package de.pocketcloud.cloud.bridge.network.packet.data;

import de.pocketcloud.cloud.bridge.network.packet.util.PacketData;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record ServerCommandExecutionResult(String id, String commandLine, List<String> messages) implements PacketData.Writable {

    public String getMessage(int index) {
        if (index < 0 || index >= messages.size()) {
            return null;
        }
        return messages.get(index);
    }

    public Map<String, Object> write() {
        Map<String, Object> data = new HashMap<>();
        data.put("id", id);
        data.put("command_line", commandLine);
        data.put("messages", messages);
        return data;
    }

    @SuppressWarnings("unchecked")
    public static ServerCommandExecutionResult read(List<Object> data) {
        if (data == null) return null;

        try {
            if (data.isEmpty()) return null;

            Map<String, Object> map = new HashMap<>();
            if (data.get(0) instanceof Map) {
                map = (Map<String, Object>) data.get(0);
            }

            if (!map.containsKey("id") || !map.containsKey("command_line") || !map.containsKey("messages")) return null;
            String id = (String) map.get("id");
            String commandLine = (String) map.get("command_line");
            List<String> messages;

            Object messagesObj = map.get("messages");
            if (messagesObj instanceof List) {
                messages = (List<String>) messagesObj;
            } else if (messagesObj instanceof Object[]) {
                messages = Arrays.asList((String[]) messagesObj);
            } else {
                return null;
            }

            return new ServerCommandExecutionResult(id, commandLine, messages);
        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }
}