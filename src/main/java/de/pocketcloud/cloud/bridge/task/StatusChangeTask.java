package de.pocketcloud.cloud.bridge.task;

import de.pocketcloud.cloud.bridge.api.object.server.util.ServerStatus;
import de.pocketcloud.cloud.bridge.api.object.template.Template;
import de.pocketcloud.cloud.bridge.api.provider.CloudServerProvider;
import de.pocketcloud.cloud.bridge.api.provider.TemplateProvider;
import dev.waterdog.waterdogpe.ProxyServer;
import dev.waterdog.waterdogpe.scheduler.Task;

public class StatusChangeTask extends Task {

    @Override
    public void onRun(int i) {
        if (CloudServerProvider.provider().current().getServerStatus() == ServerStatus.IN_GAME || CloudServerProvider.provider().current().getServerStatus() == ServerStatus.STOPPING) return;
        if (ProxyServer.getInstance().getPlayers().size() >= TemplateProvider.provider().current().getMaxPlayerCount()) {
            // Set to FULL
        } else {
            if (CloudServerProvider.provider().current().getServerStatus() == ServerStatus.FULL) {
                // Set to ONLINE
            }
        }
    }

    @Override
    public void onCancel() {}

    private int getMaxPlayers() {
        Template template = TemplateProvider.provider().current();
        return template.getMaxPlayerCount();
    }
}