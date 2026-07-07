package de.pocketcloud.cloud.bridge.network.packet.type;

import de.pocketcloud.cloud.bridge.util.Writable;
import de.pocketcloud.cloud.bridge.util.mapper.MapperUtils;

import java.util.List;
import java.util.Map;

public record ServerCommandExecutionResult(String id, String commandLine, List<String> messages) implements Writable<Map<String, Object>> {

    public String getMessage(int index) {
        if (index < 0 || index >= messages.size()) {
            return null;
        }
        return messages.get(index);
    }

    public Map<String, Object> write() {
        return MapperUtils.toMap(this);
    }

    public static ServerCommandExecutionResult read(Map<String, Object> data) {
        return MapperUtils.fromMap(data, ServerCommandExecutionResult.class);
    }
}