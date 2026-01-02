package de.pocketcloud.cloud.bridge;

import de.pocketcloud.cloud.bridge.api.CloudAPI;
import de.pocketcloud.cloud.bridge.network.Network;
import de.pocketcloud.cloud.bridge.network.packet.data.ServerDisconnectReason;
import de.pocketcloud.cloud.bridge.network.packet.impl.DisconnectPacket;
import de.pocketcloud.cloud.bridge.network.util.Address;
import de.pocketcloud.cloud.bridge.task.RequestTimeoutTask;
import de.pocketcloud.cloud.bridge.task.ServerTimeoutTask;
import de.pocketcloud.cloud.bridge.task.StatusChangeTask;
import de.pocketcloud.cloud.bridge.util.CloudEnvironmentConfig;
import dev.waterdog.waterdogpe.ProxyServer;
import dev.waterdog.waterdogpe.plugin.Plugin;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

public class CloudBridge extends Plugin {

    @Getter
    private static CloudBridge instance;

    @Getter
    @Setter
    private int lastKeepAliveCheck = 0;
    @Getter
    private CloudAPI cloudAPI;
    @Getter
    private Network network;

    @Override
    public void onStartup() {
        instance = this;
        CloudEnvironmentConfig.sync();

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

        ProxyServer.getInstance().getScheduler().scheduleRepeating(new RequestTimeoutTask(), 20);

        ProxyServer.getInstance().getScheduler().scheduleRepeating(() -> this.network.tick(), 1);

        cloudAPI.requestLogin();
    }

    public void startTasks() {
        ProxyServer.getInstance().getScheduler().scheduleRepeating(new ServerTimeoutTask(), 20);
        ProxyServer.getInstance().getScheduler().scheduleRepeating(new StatusChangeTask(), 20);
    }

    @Override
    public void onDisable() {
        this.network.sendPacket(DisconnectPacket.create(ServerDisconnectReason.SERVER_SHUTDOWN));
        this.network.close();

        ProxyServer.getInstance().shutdown();
    }
}