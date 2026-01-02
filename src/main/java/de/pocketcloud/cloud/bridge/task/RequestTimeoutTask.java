package de.pocketcloud.cloud.bridge.task;

import de.pocketcloud.cloud.bridge.network.packet.RequestPacket;
import de.pocketcloud.cloud.bridge.network.request.RequestManager;
import dev.waterdog.waterdogpe.scheduler.Task;

public class RequestTimeoutTask extends Task {

    @Override
    public void onRun(int i) {
        for (RequestPacket packet : RequestManager.getInstance().getAll().values()) {
            if ((packet.getSentTimestamp() + 10) < (System.currentTimeMillis() / 100.0)) {
                RequestManager.getInstance().reject(packet);
                RequestManager.getInstance().remove(packet);
            }
        }
    }

    @Override
    public void onCancel() {}
}