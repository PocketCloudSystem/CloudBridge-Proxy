package de.pocketcloud.cloud.bridge.api;

import de.pocketcloud.cloud.bridge.CloudBridge;
import de.pocketcloud.cloud.bridge.api.provider.*;
import de.pocketcloud.cloud.bridge.language.LanguageKey;
import de.pocketcloud.cloud.bridge.network.packet.type.LogType;
import de.pocketcloud.cloud.bridge.network.packet.type.VerificationStatus;
import de.pocketcloud.cloud.bridge.network.packet.impl.ConsoleLogPacket;
import de.pocketcloud.cloud.bridge.network.packet.impl.KeepAlivePacket;
import de.pocketcloud.cloud.bridge.network.packet.impl.request.ServerHandshakeRequestPacket;
import de.pocketcloud.cloud.bridge.network.packet.impl.response.ServerHandshakeResponsePacket;
import de.pocketcloud.cloud.bridge.util.CloudEnvironmentConfig;
import de.pocketcloud.cloud.bridge.util.Utils;
import dev.waterdog.waterdogpe.ProxyServer;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public final class CloudAPI {

    private static CloudAPI instance;
    
    @Getter
    private VerificationStatus verificationStatus = VerificationStatus.PENDING;
    private final Map<Class<? extends CloudAPIProvider>, CloudAPIProvider> providers = new HashMap<>();
    
    public CloudAPI() {
        instance = this;

        registerProvider(new TemplateProvider());
        registerProvider(new CloudServerProvider());
        registerProvider(new ServerGroupProvider());
        registerProvider(new CloudPlayerProvider());
    }

    public void requestLogin() {
        String serverName = CloudEnvironmentConfig.getServerName();
        int processId = (int) ProcessHandle.current().pid();
        int maxPlayers = ProxyServer.getInstance().getConfiguration().getMaxPlayerCount();

         ServerHandshakeRequestPacket.create(serverName, processId, maxPlayers).sendRequest()
             .then((response, value) -> {
                 ServerHandshakeResponsePacket packet = (ServerHandshakeResponsePacket) response;
                 VerificationStatus status = packet.getVerifyStatus();
                 this.verificationStatus = status;

                 if (status == VerificationStatus.VERIFIED) {
                     CloudBridge.getInstance().setLastKeepAliveCheck(Utils.time());
                     CloudBridge.getInstance().startTasks();
                     CloudBridge.getInstance().getLogger().info(LanguageKey.INGAME_SERVER_VERIFIED.translate());
                     KeepAlivePacket.create().sendPacket();
                 } else {
                     CloudBridge.getInstance().getLogger().warn("Cloud responded with verification status '{}', shutting down...", status.getName());
                     ProxyServer.getInstance().shutdown();
                 }

                 return null;
             })
             .failure((request, exception) -> {
                 CloudBridge.getInstance().getLogger().warn("Cloud did not respond to ServerHandshakeRequestPacket, shutting down...");
                 ProxyServer.getInstance().shutdown();
             });
    }

    public void logConsole(String message) {
        logConsole(message, LogType.INFO);
    }

    public void logConsole(String message, LogType logType) {
        ConsoleLogPacket.create(message, logType).sendPacket();
    }

    public void registerProvider(CloudAPIProvider provider) {
        providers.put(provider.getClass(), provider);
    }

    @SuppressWarnings("unchecked")
    public <T extends CloudAPIProvider> T getProvider(Class<T> providerClass) {
        return (T) providers.get(providerClass);
    }

    public static CloudAPI get() {
        if (instance == null) instance = new CloudAPI();
        return instance;
    }
}