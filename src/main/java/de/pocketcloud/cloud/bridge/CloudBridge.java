package de.pocketcloud.cloud.bridge;

import de.pocketcloud.cloud.bridge.api.CloudAPI;
import de.pocketcloud.cloud.bridge.handler.ConnectAndReconnectHandler;
import de.pocketcloud.cloud.bridge.language.LanguageKey;
import de.pocketcloud.cloud.bridge.listener.EventListener;
import de.pocketcloud.cloud.bridge.network.Network;
import de.pocketcloud.cloud.bridge.network.packet.data.ServerDisconnectReason;
import de.pocketcloud.cloud.bridge.network.packet.impl.DisconnectPacket;
import de.pocketcloud.cloud.bridge.network.util.Address;
import de.pocketcloud.cloud.bridge.task.RequestTimeoutTask;
import de.pocketcloud.cloud.bridge.task.ServerTimeoutTask;
import de.pocketcloud.cloud.bridge.task.StatusChangeTask;
import de.pocketcloud.cloud.bridge.util.CloudEnvironmentConfig;
import dev.waterdog.waterdogpe.ProxyServer;
import dev.waterdog.waterdogpe.event.defaults.InitialServerDeterminedEvent;
import dev.waterdog.waterdogpe.event.defaults.PlayerDisconnectedEvent;
import dev.waterdog.waterdogpe.event.defaults.PlayerLoginEvent;
import dev.waterdog.waterdogpe.event.defaults.ServerTransferEvent;
import dev.waterdog.waterdogpe.network.protocol.ProtocolVersion;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import dev.waterdog.waterdogpe.plugin.Plugin;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CloudBridge extends Plugin {

    @Getter
    private static CloudBridge instance;

    @Getter
    @Setter
    private double lastKeepAliveCheck = 0;
    @Getter
    private CloudAPI cloudAPI;
    @Getter
    private Network network;

    @Override
    public void onStartup() {
        instance = this;
        CloudEnvironmentConfig.sync();

        saveResource("config.yml");

        this.cloudAPI = new CloudAPI();
        this.network = new Network(Address.create(CloudEnvironmentConfig.getNetworkAddress(), CloudEnvironmentConfig.getNetworkPort()));
    }

    @Override
    public void onEnable() {
        try {
            network.init();
        } catch (IOException e) {
            this.getLogger().error("Error while initializing network", e);
            ProxyServer.getInstance().shutdown();
            return;
        }

        network.start();

        ProxyServer.getInstance().setJoinHandler(new ConnectAndReconnectHandler());
        ProxyServer.getInstance().setReconnectHandler(new ConnectAndReconnectHandler());

        ProxyServer.getInstance().getScheduler().scheduleRepeating(new RequestTimeoutTask(), 20);
        ProxyServer.getInstance().getEventManager().subscribe(PlayerLoginEvent.class, EventListener::onLogin);
        ProxyServer.getInstance().getEventManager().subscribe(PlayerDisconnectedEvent.class, EventListener::onDisconnect);
        ProxyServer.getInstance().getEventManager().subscribe(ServerTransferEvent.class, EventListener::onServerSwitch);
        ProxyServer.getInstance().getEventManager().subscribe(InitialServerDeterminedEvent.class, EventListener::onInitialServerDetermined);

        ProxyServer.getInstance().getScheduler().scheduleRepeating(() -> this.network.tick(), 1);

        cloudAPI.requestLogin();
    }

    public void startTasks() {
        ProxyServer.getInstance().getScheduler().scheduleRepeating(new ServerTimeoutTask(), 20);
        ProxyServer.getInstance().getScheduler().scheduleRepeating(new StatusChangeTask(), 20);
    }

    @Override
    public void onDisable() {
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers().values()) player.disconnect((CharSequence) LanguageKey.INGAME_PROXY_STOPPED.translate());
        this.network.sendPacket(DisconnectPacket.create(ServerDisconnectReason.SERVER_SHUTDOWN));
        this.network.close();

        ProxyServer.getInstance().shutdown();
    }

    public List<Integer> getAcceptedProtocols() {
        List<Integer> acceptedProtocols = new ArrayList<>(ProtocolVersion.latest().getProtocol());
        return getConfig().getList("acceptedProtocols", acceptedProtocols);
    }
}