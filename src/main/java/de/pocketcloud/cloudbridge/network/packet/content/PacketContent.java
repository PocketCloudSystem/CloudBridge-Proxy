package de.pocketcloud.cloudbridge.network.packet.content;

import de.pocketcloud.cloudbridge.api.player.CloudPlayer;
import de.pocketcloud.cloudbridge.api.server.CloudServer;
import de.pocketcloud.cloudbridge.api.server.status.ServerStatus;
import de.pocketcloud.cloudbridge.api.template.Template;
import de.pocketcloud.cloudbridge.network.packet.impl.types.DisconnectReason;
import de.pocketcloud.cloudbridge.network.packet.impl.types.ErrorReason;
import de.pocketcloud.cloudbridge.network.packet.impl.types.TextType;
import de.pocketcloud.cloudbridge.network.packet.impl.types.VerifyStatus;
import lombok.NoArgsConstructor;

import java.util.*;

@NoArgsConstructor
public class PacketContent {

    private ArrayList<Object> content = new ArrayList<>();

    public PacketContent(ArrayList<Object> content) {
        this.content = content;
    }

    public void put(Object v) {
        content.add(v);
    }

    public void putServerStatus(ServerStatus serverStatus) {
        put(serverStatus.getName());
    }

    public void putVerifyStatus(VerifyStatus verifyStatus) {
        put(verifyStatus.getName());
    }

    public void putDisconnectReason(DisconnectReason disconnectReason) {
        put(disconnectReason.getName());
    }

    public void putTextType(TextType textType) {
        put(textType.getName());
    }

    public void putServer(CloudServer server) {
        put(server.toArray());
    }

    public void putTemplate(Template template) {
        put(template.toArray());
    }

    public void putPlayer(CloudPlayer player) {
        put(player.toArray());
    }

    public void putErrorReason(ErrorReason errorReason) {
        put(errorReason.getName());
    }

    public Object read() {
        if (content.size() > 0) {
            Object get = content.get(0);
            content.remove(0);
            Collection<Object> oldContent = new ArrayList<>(content);
            content.clear();
            content.addAll(oldContent);
            return get;
        }
        return null;
    }

    public String readString() {
        try {
            return read().toString();
        } catch (Exception e) {
            return "";
        }
    }

    public Integer readInt() {
        try {
            Object read = read();
            return (read instanceof Integer ? (Integer) read : 0);
        } catch (Exception e) {
            return 0;
        }
    }

    public Double readDouble() {
        try {
            Object read = read();
            return (read instanceof Double ? (Double) read : 0.0);
        } catch (Exception e) {
            return 0.0;
        }
    }

    public Float readFloat() {
        try {
            Object read = read();
            return (read instanceof Float ? (Float) read : 0.0f);
        } catch (Exception e) {
            return 0.0f;
        }
    }

    public Boolean readBool() {
        try {
            Object read = read();
            return (read instanceof Boolean ? (Boolean) read : false);
        } catch (Exception e) {
            return false;
        }
    }

    public ArrayList<?> readArrayList() {
        try {
            Object read = read();
            return (read instanceof ArrayList<?> ? (ArrayList<?>) read : new ArrayList<>());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public Map<?,?> readMap() {
        try {
            Object read = read();
            return (read instanceof Map<?,?> ? (Map<?, ?>) read : Map.of());
        } catch (Exception e) {
            return Map.of();
        }
    }

    public ServerStatus readServerStatus() {
        try {
            return ServerStatus.getServerStatusByName(readString());
        } catch (Exception e) {
            return null;
        }
    }

    public VerifyStatus readVerifyStatus() {
        try {
            return VerifyStatus.getStatusByName(readString());
        } catch (Exception e) {
            return null;
        }
    }

    public DisconnectReason readDisconnectReason() {
        try {
            return DisconnectReason.getReasonByName(readString());
        } catch (Exception e) {
            return null;
        }
    }

    public TextType readTextType() {
        try {
            return TextType.getTypeByName(readString());
        } catch (Exception e) {
            return null;
        }
    }

    public CloudServer readServer() {
        try {
            return CloudServer.fromArray(readMap());
        } catch (Exception e) {
            return null;
        }
    }

    public Template readTemplate() {
        try {
            return Template.fromArray(readMap());
        } catch (Exception e) {
            return null;
        }
    }

    public CloudPlayer readPlayer() {
        try {
            return CloudPlayer.fromArray(readMap());
        } catch (Exception e) {
            return null;
        }
    }

    public ErrorReason readErrorReason() {
        try {
            return ErrorReason.getReasonByName(readString());
        } catch (Exception e) {
            return null;
        }
    }

    public ArrayList<Object> getContent() {
        return content;
    }
}
