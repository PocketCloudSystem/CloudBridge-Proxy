package de.pocketcloud.cloudbridge.task;

import de.pocketcloud.cloudbridge.api.CloudAPI;
import de.pocketcloud.cloudbridge.api.server.status.ServerStatus;
import dev.waterdog.waterdogpe.ProxyServer;
import dev.waterdog.waterdogpe.scheduler.Task;

public class ChangeStatusTask extends Task {

    @Override
    public void onRun(int currentTick) {
        if (CloudAPI.getInstance().getCurrentServer() == null) return;
        if (CloudAPI.getInstance().getCurrentServer().getServerStatus() == ServerStatus.IN_GAME || CloudAPI.getInstance().getCurrentServer().getServerStatus() == ServerStatus.STOPPING) return;
        if (ProxyServer.getInstance().getPlayers().size() >= ProxyServer.getInstance().getConfiguration().getMaxPlayerCount()) {
            CloudAPI.getInstance().changeStatus(ServerStatus.FULL);
        } else {
            if (CloudAPI.getInstance().getCurrentServer().getServerStatus() == ServerStatus.FULL) {
                CloudAPI.getInstance().changeStatus(ServerStatus.ONLINE);
            }
        }
    }

    @Override
    public void onCancel() {

    }
}
