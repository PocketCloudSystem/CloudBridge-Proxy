package de.pocketcloud.cloud.bridge.api.object.player;

import de.pocketcloud.cloud.bridge.api.object.server.CloudServer;
import de.pocketcloud.cloud.bridge.network.packet.type.TextType;
import de.pocketcloud.cloud.bridge.network.packet.impl.PlayerKickPacket;
import de.pocketcloud.cloud.bridge.network.packet.impl.PlayerTextPacket;
import de.pocketcloud.cloud.bridge.api.provider.CloudServerProvider;
import de.pocketcloud.cloud.bridge.util.Writable;
import de.pocketcloud.cloud.bridge.util.mapper.MapperUtils;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import lombok.Getter;

import java.util.Map;

public final class CloudPlayer implements Writable<Map<String, Object>> {

    @Getter
    private final String name;
    @Getter
    private final String address;
    @Getter
    private final String xboxUserId;
    @Getter
    private final String uniqueId;
    private String currentServer;
    private String currentProxy;
    
    public CloudPlayer(String name, String address, String xboxUserId, String uniqueId, String currentServer, String currentProxy) {
        this.name = name;
        this.address = address;
        this.xboxUserId = xboxUserId;
        this.uniqueId = uniqueId;
        this.currentServer = currentServer;
        this.currentProxy = currentProxy;
    }

    public void sync(Map<String, Object> data) {
        if (data.containsKey("currentServer")) {
            this.currentServer = (String) data.get("currentServer");
        }
        if (data.containsKey("currentProxy")) {
            this.currentProxy = (String) data.get("currentProxy");
        }
    }

    public void send(String message, TextType textType) {
        PlayerTextPacket.create(name, message, textType).sendPacket();
    }

    public void sendMessage(String message) {
        send(message, TextType.MESSAGE);
    }

    public void sendPopup(String message) {
        send(message, TextType.POPUP);
    }

    public void sendTip(String message) {
        send(message, TextType.TIP);
    }

    public void sendTitle(String message) {
        send(message, TextType.TITLE);
    }

    public void sendActionBarMessage(String message) {
        send(message, TextType.ACTION_BAR);
    }

    public void sendToastNotification(String title, String body) {
        send(title + "\n" + body, TextType.TOAST_NOTIFICATION);
    }

    public void kick() {
        kick("", "");
    }

    public void kick(String reason) {
        kick(reason, "");
    }

    public void kick(String reason, String disconnectScreenMessage) {
        PlayerKickPacket.create(name, reason, disconnectScreenMessage).sendPacket();
    }

    public void setCurrentServer(String currentServer) {
        this.currentServer = currentServer;
    }
    
    public void setCurrentServer(CloudServer currentServer) {
        this.currentServer = currentServer != null ? currentServer.getName() : null;
    }
    
    public void setCurrentProxy(String currentProxy) {
        this.currentProxy = currentProxy;
    }
    
    public void setCurrentProxy(CloudServer currentProxy) {
        this.currentProxy = currentProxy != null ? currentProxy.getName() : null;
    }
    
    public CloudServer getCurrentServer() {
        return CloudServerProvider.provider().get(currentServer);
    }
    
    public CloudServer getCurrentProxy() {
        return CloudServerProvider.provider().get(currentProxy);
    }
    
    public String getCurrentServerName() {
        return currentServer;
    }
    
    public String getCurrentProxyName() {
        return currentProxy;
    }
    
    @Override
    public Map<String, Object> write() {
        return MapperUtils.toMap(this);
    }
    
    public static CloudPlayer read(Map<String, Object> data) {
        return MapperUtils.fromMap(data, CloudPlayer.class);
    }

    public static CloudPlayer fromProxiedPlayer(ProxiedPlayer proxiedPlayer) {
        return new CloudPlayer(proxiedPlayer.getName(), proxiedPlayer.getAddress().getHostString(), proxiedPlayer.getXuid(), proxiedPlayer.getUniqueId().toString(), null, null);
    }
}