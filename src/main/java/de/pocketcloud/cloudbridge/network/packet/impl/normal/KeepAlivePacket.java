package de.pocketcloud.cloudbridge.network.packet.impl.normal;

import de.pocketcloud.cloudbridge.CloudBridge;
import de.pocketcloud.cloudbridge.network.Network;
import de.pocketcloud.cloudbridge.network.packet.CloudPacket;
import de.pocketcloud.cloudbridge.util.Utils;
import dev.waterdog.waterdogpe.logger.MainLogger;

public class KeepAlivePacket extends CloudPacket {

    @Override
    public void handle() {
        CloudBridge.getInstance().lastKeepALiveCheck = Utils.time();
        Network.getInstance().sendPacket(new KeepAlivePacket());
    }
}