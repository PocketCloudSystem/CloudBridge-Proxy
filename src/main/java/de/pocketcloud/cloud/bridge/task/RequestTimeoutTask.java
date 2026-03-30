package de.pocketcloud.cloud.bridge.task;

import de.pocketcloud.cloud.bridge.network.packet.RequestPacket;
import de.pocketcloud.cloud.bridge.network.request.RequestManager;
import de.pocketcloud.cloud.bridge.util.Utils;
import dev.waterdog.waterdogpe.scheduler.Task;

final public class RequestTimeoutTask extends Task {

    @Override
    public void onRun(int i) {
        for (RequestPacket packet : RequestManager.getInstance().getAll().values()) {
            if ((packet.getSentTimestamp() + 10) < Utils.time()) {
                RequestManager.getInstance().reject(packet);
                RequestManager.getInstance().remove(packet);
            }
        }
    }

    @Override
    public void onCancel() {}
}