package de.pocketcloud.cloudbridge.task;

import de.pocketcloud.cloudbridge.CloudBridge;
import de.pocketcloud.cloudbridge.api.CloudAPI;
import de.pocketcloud.cloudbridge.util.Utils;
import dev.waterdog.waterdogpe.ProxyServer;
import dev.waterdog.waterdogpe.logger.MainLogger;
import dev.waterdog.waterdogpe.scheduler.Task;

public class TimeoutTask extends Task {

    @Override
    public void onRun(int i) {
        if (!CloudAPI.getInstance().isVerified()) return;
        if ((CloudBridge.getInstance().lastKeepALiveCheck + 10) <= Utils.time()) {
            MainLogger.getLogger().warning("§cServer timeout! Shutdown...");
            ProxyServer.getInstance().shutdown();
        }
    }

    @Override
    public void onCancel() {

    }
}
