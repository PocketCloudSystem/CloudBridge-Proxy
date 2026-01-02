package de.pocketcloud.cloud.bridge.task;

import de.pocketcloud.cloud.bridge.CloudBridge;
import de.pocketcloud.cloud.bridge.util.CloudEnvironmentConfig;
import dev.waterdog.waterdogpe.ProxyServer;
import dev.waterdog.waterdogpe.scheduler.Task;

public class ServerTimeoutTask extends Task {

    @Override
    public void onRun(int i) {
        if ((CloudBridge.getInstance().getLastKeepAliveCheck() + CloudEnvironmentConfig.getServerTimeout()) <= (System.currentTimeMillis() / 1000L)) {
            CloudBridge.getInstance().getLogger().warn("§cServer timed out, shutting this instance down...");
            ProxyServer.getInstance().shutdown();
        }
    }

    @Override
    public void onCancel() {}
}