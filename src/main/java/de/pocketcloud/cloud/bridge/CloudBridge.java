package de.pocketcloud.cloud.bridge;

import de.pocketcloud.cloud.bridge.api.CloudAPI;
import de.pocketcloud.cloud.bridge.handler.ConnectAndReconnectHandler;
import de.pocketcloud.cloud.bridge.language.LanguageKey;
import de.pocketcloud.cloud.bridge.listener.EventListener;
import de.pocketcloud.cloud.bridge.network.NetworkNettyClient;
import de.pocketcloud.cloud.bridge.network.packet.type.ServerDisconnectReason;
import de.pocketcloud.cloud.bridge.network.packet.impl.DisconnectPacket;
import de.pocketcloud.cloud.bridge.task.RequestTimeoutTask;
import de.pocketcloud.cloud.bridge.task.ServerTimeoutTask;
import de.pocketcloud.cloud.bridge.task.StatusChangeTask;
import de.pocketcloud.network.packet.PacketPool;
import de.pocketcloud.network.traffic.TrafficMonitorManager;
import de.pocketcloud.cloud.bridge.util.CloudEnvironmentConfig;
import de.pocketcloud.cloud.bridge.util.ProcessUtils;
import dev.waterdog.waterdogpe.ProxyServer;
import dev.waterdog.waterdogpe.event.defaults.InitialServerDeterminedEvent;
import dev.waterdog.waterdogpe.event.defaults.PlayerDisconnectedEvent;
import dev.waterdog.waterdogpe.event.defaults.PlayerLoginEvent;
import dev.waterdog.waterdogpe.event.defaults.ServerTransferEvent;
import dev.waterdog.waterdogpe.network.protocol.ProtocolVersion;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import dev.waterdog.waterdogpe.plugin.Plugin;
import de.pocketcloud.cloud.bridge.network.packet.impl.*;
import de.pocketcloud.cloud.bridge.network.packet.impl.request.*;
import de.pocketcloud.cloud.bridge.network.packet.impl.request.client.CommandExecuteRequestPacket;
import de.pocketcloud.cloud.bridge.network.packet.impl.response.*;
import de.pocketcloud.cloud.bridge.network.packet.impl.response.client.CommandExecuteResponsePacket;

import lombok.Getter;
import lombok.Setter;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

@Getter
public class CloudBridge extends Plugin {

    @Getter
    private static CloudBridge instance;

    @Setter
    private double lastKeepAliveCheck = 0;
    private CloudAPI cloudAPI;
    private NetworkNettyClient network;
    private TrafficMonitorManager trafficMonitorManager;
    private PacketPool packetPool;

    @Override
    public void onStartup() {
        instance = this;
        CloudEnvironmentConfig.sync();

        saveResource("config.yml");

        this.cloudAPI = new CloudAPI();
        this.network = new NetworkNettyClient(new InetSocketAddress(CloudEnvironmentConfig.getNetworkAddress(), CloudEnvironmentConfig.getNetworkPort()));
        this.trafficMonitorManager = new TrafficMonitorManager();
        this.packetPool = new PacketPool(pool -> {
            pool.register(ServerHandshakeRequestPacket.class, ServerHandshakeRequestPacket::new);
            pool.register(ServerHandshakeResponsePacket.class, ServerHandshakeResponsePacket::new);
            pool.register(DisconnectPacket.class, DisconnectPacket::new);
            pool.register(CloudNotificationPacket.class, CloudNotificationPacket::new);
            pool.register(KeepAlivePacket.class, KeepAlivePacket::new);
            pool.register(CommandExecuteRequestPacket.class, CommandExecuteRequestPacket::new);
            pool.register(CommandExecuteResponsePacket.class, CommandExecuteResponsePacket::new);
            pool.register(LanguageSyncPacket.class, LanguageSyncPacket::new);
            pool.register(LibrarySyncPacket.class, LibrarySyncPacket::new);
            pool.register(ModuleSyncPacket.class, ModuleSyncPacket::new);
            pool.register(TemplateSyncPacket.class, TemplateSyncPacket::new);
            pool.register(ServerSyncPacket.class, ServerSyncPacket::new);
            pool.register(ServerGroupSyncPacket.class, ServerGroupSyncPacket::new);
            pool.register(PlayerSyncPacket.class, PlayerSyncPacket::new);
            pool.register(PlayerConnectPacket.class, PlayerConnectPacket::new);
            pool.register(PlayerDisconnectPacket.class, PlayerDisconnectPacket::new);
            pool.register(PlayerKickPacket.class, PlayerKickPacket::new);
            pool.register(PlayerSwitchServerPacket.class, PlayerSwitchServerPacket::new);
            pool.register(ProxyRegisterServerPacket.class, ProxyRegisterServerPacket::new);
            pool.register(ProxyUnregisterServerPacket.class, ProxyUnregisterServerPacket::new);
            pool.register(PlayerNotificationCheckRequestPacket.class, PlayerNotificationCheckRequestPacket::new);
            pool.register(PlayerNotificationCheckResponsePacket.class, PlayerNotificationCheckResponsePacket::new);
            pool.register(PlayerWhitelistCheckRequestPacket.class, PlayerWhitelistCheckRequestPacket::new);
            pool.register(PlayerWhitelistCheckResponsePacket.class, PlayerWhitelistCheckResponsePacket::new);
            pool.register(PlayerUpdateNotificationStatePacket.class, PlayerUpdateNotificationStatePacket::new);
            pool.register(MaintenanceListSyncPacket.class, MaintenanceListSyncPacket::new);
            pool.register(NotificationListSyncPacket.class, NotificationListSyncPacket::new);
            pool.register(ServerChangeStatusPacket.class, ServerChangeStatusPacket::new);
            pool.register(CloudSyncServerStoragePacket.class, CloudSyncServerStoragePacket::new);
            pool.register(PlayerTransferPacket.class, PlayerTransferPacket::new);
            pool.register(PlayerTextPacket.class, PlayerTextPacket::new);
            pool.register(ServerStartRequestPacket.class, ServerStartRequestPacket::new);
            pool.register(ServerStartResponsePacket.class, ServerStartResponsePacket::new);
            pool.register(ServerStopRequestPacket.class, ServerStopRequestPacket::new);
            pool.register(ServerStopResponsePacket.class, ServerStopResponsePacket::new);
            pool.register(ServerSaveRequestPacket.class, ServerSaveRequestPacket::new);
            pool.register(ServerSaveResponsePacket.class, ServerSaveResponsePacket::new);
            pool.register(ConsoleLogPacket.class, ConsoleLogPacket::new);
            pool.register(BulkSyncPacket.class, BulkSyncPacket::new);
        });

        this.packetPool.load();
    }

    @Override
    public void onEnable() {
        network.start();

        ProxyServer.getInstance().setJoinHandler(new ConnectAndReconnectHandler());
        ProxyServer.getInstance().setReconnectHandler(new ConnectAndReconnectHandler());

        ProxyServer.getInstance().getScheduler().scheduleRepeating(new RequestTimeoutTask(), 20);
        ProxyServer.getInstance().getEventManager().subscribe(PlayerLoginEvent.class, EventListener::onLogin);
        ProxyServer.getInstance().getEventManager().subscribe(PlayerDisconnectedEvent.class, EventListener::onDisconnect);
        ProxyServer.getInstance().getEventManager().subscribe(ServerTransferEvent.class, EventListener::onServerSwitch);
        ProxyServer.getInstance().getEventManager().subscribe(InitialServerDeterminedEvent.class, EventListener::onInitialServerDetermined);

        ProxyServer.getInstance().getScheduler().scheduleRepeating(() -> {
            this.trafficMonitorManager.tick(ProxyServer.getInstance().getCurrentTick());
        }, 1);

        ProcessUtils.startCpuRetrieveCycle();
        cloudAPI.requestLogin();
        ProxyServer.getInstance().getScheduler().scheduleDelayedRepeating(ProcessUtils::restartCpuRetrieveCycle, 40, 40);
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
        List<Integer> defaultProtocols = new ArrayList<>(List.of(ProtocolVersion.latest().getProtocol()));
        return getConfig().getList("acceptedProtocols", defaultProtocols);
    }
}