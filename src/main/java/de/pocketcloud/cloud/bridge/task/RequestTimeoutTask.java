package de.pocketcloud.cloud.bridge.task;

import de.pocketcloud.cloud.bridge.network.packet.RequestPacket;
import de.pocketcloud.cloud.bridge.network.packet.impl.request.ServerHandshakeRequestPacket;
import de.pocketcloud.cloud.bridge.network.request.RequestManager;
import de.pocketcloud.cloud.bridge.util.CloudEnvironmentConfig;
import de.pocketcloud.cloud.bridge.util.Utils;
import dev.waterdog.waterdogpe.scheduler.Task;

final public class RequestTimeoutTask extends Task {

    @Override
    public void onRun(int i) {
        for (RequestPacket packet : RequestManager.getInstance().getAll().values()) {
            int timeout = packet instanceof ServerHandshakeRequestPacket ? CloudEnvironmentConfig.getServerTimeout() : 10;
            if ((packet.getSentTimestamp() + timeout) < Utils.time()) {
                RequestManager.getInstance().reject(packet);
                RequestManager.getInstance().remove(packet);
            }
        }
    }

    @Override
    public void onCancel() {}
}