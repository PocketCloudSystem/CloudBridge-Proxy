package de.pocketcloud.cloud.bridge.network.packet.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import de.pocketcloud.cloud.bridge.api.object.group.ServerGroup;
import de.pocketcloud.cloud.bridge.api.object.player.CloudPlayer;
import de.pocketcloud.cloud.bridge.api.object.server.CloudServer;
import de.pocketcloud.cloud.bridge.api.object.server.util.ServerStatus;
import de.pocketcloud.cloud.bridge.api.object.template.Template;
import de.pocketcloud.cloud.bridge.network.packet.data.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public final class PacketData {

    private final List<Object> data;
    private int readIndex = 0;
    private static final Gson GSON = new Gson();

    public PacketData() {
        this.data = new ArrayList<>();
    }

    public PacketData(List<Object> data) {
        this.data = new ArrayList<>(data);
    }

    public PacketData write(Object value) {
        data.add(value);
        return this;
    }

    public void writeAll(Object... values) {
        for (Object item : values) {
            if (item instanceof Writable writable) {
                write(writable.write());
            } else {
                write(item);
            }
        }
    }

    public Object read() {
        if (readIndex >= data.size()) return null;
        return data.get(readIndex++);
    }

    public void readAll(Object... refs) {
        for (int i = 0; i < refs.length; i++) {
            if (isEmpty()) throw new NoSuchElementException("Passed too many references, packet buffer is empty");
            refs[i] = read();
        }
    }

    public String readString() {
        Object read = read();
        if (read == null) return null;
        return String.valueOf(read);
    }

    public Integer readInt() {
        Object read = read();
        if (read == null) return null;
        if (read instanceof Number number) {
            return number.intValue();
        }
        return Integer.parseInt(String.valueOf(read));
    }

    public Float readFloat() {
        Object read = read();
        if (read == null) return null;
        if (read instanceof Number number) {
            return number.floatValue();
        }
        return Float.parseFloat(String.valueOf(read));
    }

    public Double readDouble() {
        Object read = read();
        if (read == null) return null;
        if (read instanceof Number number) {
            return number.doubleValue();
        }
        return Double.parseDouble(String.valueOf(read));
    }

    public Boolean readBool() {
        Object read = read();
        if (read == null) return null;
        if (read instanceof Boolean bool) {
            return bool;
        }
        return Boolean.parseBoolean(String.valueOf(read));
    }

    @SuppressWarnings("unchecked")
    public List<Object> readArray() {
        Object read = read();
        if (read instanceof List) {
            return (List<Object>) read;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> readMap() {
        Object read = read();
        if (read instanceof Map) {
            return (Map<String, Object>) read;
        }
        return null;
    }

    public Template readTemplate() {
        return Template.read(readMap());
    }

    public CloudServer readServer() {
        return CloudServer.read(readMap());
    }

    public ServerGroup readServerGroup() {
        return ServerGroup.read(readMap());
    }

    public CloudPlayer readPlayer() {
        return CloudPlayer.read(readMap());
    }

    public ServerStatus readServerStatus() {
        return ServerStatus.fromName(readString());
    }

    public LogType readLogType() {
        return LogType.fromName(readString());
    }

    public NotificationType readNotificationType() {
        return NotificationType.fromName(readString());
    }

    public ServerCommandExecutionResult readServerCommandExecutionResult() {
        return ServerCommandExecutionResult.read(readArray());
    }

    public ServerDisconnectReason readServerDisconnectReason() {
        return ServerDisconnectReason.fromName(readString());
    }

    public ServerErrorReason readServerErrorReason() {
        return ServerErrorReason.fromName(readString());
    }

    public VerifyStatus readVerifyStatus() {
        return VerifyStatus.fromName(readString());
    }

    public TextType readTextType() {
        return TextType.fromName(readString());
    }

    public boolean isEmpty() {
        return readIndex >= data.size();
    }

    public int count() {
        return data.size() - readIndex;
    }

    public List<Object> getData() {
        return new ArrayList<>(data);
    }

    public String toJson() {
        return GSON.toJson(data);
    }

    public static PacketData fromJson(String json) {
        JsonArray jsonArray = GSON.fromJson(json, JsonArray.class);
        List<Object> list = new ArrayList<>();
        for (JsonElement element : jsonArray) {
            if (element.isJsonPrimitive()) {
                var primitive = element.getAsJsonPrimitive();
                if (primitive.isBoolean()) {
                    list.add(primitive.getAsBoolean());
                } else if (primitive.isNumber()) {
                    list.add(primitive.getAsNumber());
                } else {
                    list.add(primitive.getAsString());
                }
            } else if (element.isJsonArray()) {
                List<Object> subList = new ArrayList<>();
                for (JsonElement subElement : element.getAsJsonArray()) {
                    subList.add(subElement);
                }
                list.add(subList);
            } else if (element.isJsonObject()) {
                list.add(element.getAsJsonObject());
            } else {
                list.add(element);
            }
        }
        return new PacketData(list);
    }

    public interface Writable {

        Object write();
    }
}