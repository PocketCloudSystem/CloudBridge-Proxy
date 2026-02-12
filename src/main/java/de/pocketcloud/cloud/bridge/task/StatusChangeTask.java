package de.pocketcloud.cloud.bridge.task;

import de.pocketcloud.cloud.bridge.api.object.server.util.ServerStatus;
import de.pocketcloud.cloud.bridge.api.object.template.Template;
import de.pocketcloud.cloud.bridge.api.provider.CloudServerProvider;
import de.pocketcloud.cloud.bridge.api.provider.TemplateProvider;
import dev.waterdog.waterdogpe.ProxyServer;
import dev.waterdog.waterdogpe.scheduler.Task;

final public class StatusChangeTask extends Task {

    @Override
    public void onRun(int i) {
        if (CloudServerProvider.provider().current().getServerStatus() == ServerStatus.IN_GAME || CloudServerProvider.provider().current().getServerStatus() == ServerStatus.STOPPING) return;
        if (ProxyServer.getInstance().getPlayers().size() >= TemplateProvider.provider().current().getMaxPlayerCount()) {
            CloudServerProvider.provider().current().setServerStatus(ServerStatus.FULL);
        } else {
            if (CloudServerProvider.provider().current().getServerStatus() == ServerStatus.FULL) {
                CloudServerProvider.provider().current().setServerStatus(ServerStatus.ONLINE);
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