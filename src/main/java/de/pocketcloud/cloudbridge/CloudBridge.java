package de.pocketcloud.cloudbridge;

import de.pocketcloud.cloudbridge.api.CloudAPI;
import de.pocketcloud.cloudbridge.handler.JoinHandler;
import de.pocketcloud.cloudbridge.handler.ReconnectHandler;
import de.pocketcloud.cloudbridge.listener.EventListener;
import de.pocketcloud.cloudbridge.network.Network;
import de.pocketcloud.cloudbridge.network.packet.impl.normal.DisconnectPacket;
import de.pocketcloud.cloudbridge.network.packet.impl.types.DisconnectReason;
import de.pocketcloud.cloudbridge.network.request.RequestManager;
import de.pocketcloud.cloudbridge.task.ChangeStatusTask;
import de.pocketcloud.cloudbridge.task.TimeoutTask;
import de.pocketcloud.cloudbridge.utils.Utils;
import dev.waterdog.waterdogpe.ProxyServer;
import dev.waterdog.waterdogpe.event.defaults.PlayerDisconnectedEvent;
import dev.waterdog.waterdogpe.event.defaults.PlayerLoginEvent;
import dev.waterdog.waterdogpe.event.defaults.ServerTransferRequestEvent;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import dev.waterdog.waterdogpe.plugin.Plugin;
import dev.waterdog.waterdogpe.utils.config.JsonConfig;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CloudBridge extends Plugin {

    private static CloudBridge instance;
    private final ExecutorService threadPool = Executors.newCachedThreadPool();
    private CloudAPI cloudAPI;
    private Network network;
    private RequestManager requestManager;
    public long lastKeepALiveCheck = 0;

    @Override
    public void onEnable() {
        instance = this;

        cloudAPI = new CloudAPI();
        try {
            network = new Network(new InetSocketAddress(InetAddress.getByName("127.0.0.1"), CloudAPI.getInstance().getCloudPort()));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        requestManager = new RequestManager();

        ProxyServer.getInstance().getEventManager().subscribe(PlayerLoginEvent.class, EventListener::onLogin);
        ProxyServer.getInstance().getEventManager().subscribe(PlayerDisconnectedEvent.class, EventListener::onDisconnected);
        ProxyServer.getInstance().getEventManager().subscribe(ServerTransferRequestEvent.class, EventListener::onTransfer);

        lastKeepALiveCheck = Utils.microtime();
        ProxyServer.getInstance().getScheduler().scheduleRepeating(new ChangeStatusTask(), 20);
        ProxyServer.getInstance().getScheduler().scheduleRepeating(new TimeoutTask(), 20);

        ProxyServer.getInstance().setJoinHandler(new JoinHandler());
        ProxyServer.getInstance().setReconnectHandler(new ReconnectHandler());

        cloudAPI.processLogin();
    }

    @Override
    public void onDisable() {
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers().values()) player.disconnect(new JsonConfig(CloudAPI.getInstance().getCloudPath() + "/storage/inGame/messages.json").getString("proxy-stopped"));
        network.sendPacket(new DisconnectPacket(DisconnectReason.SERVER_SHUTDOWN));
        network.close();
        threadPool.shutdownNow();
    }

    public RequestManager getRequestManager() {
        return requestManager;
    }

    public Network getNetwork() {
        return network;
    }

    public CloudAPI getCloudAPI() {
        return cloudAPI;
    }

    public ExecutorService getThreadPool() {
        return threadPool;
    }

    public static CloudBridge getInstance() {
        return instance;
    }
}