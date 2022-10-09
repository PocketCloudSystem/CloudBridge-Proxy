package de.pocketcloud.cloudbridge.listener;

import de.pocketcloud.cloudbridge.api.CloudAPI;
import de.pocketcloud.cloudbridge.api.player.CloudPlayer;
import de.pocketcloud.cloudbridge.network.Network;
import de.pocketcloud.cloudbridge.network.packet.impl.normal.CloudPlayerSwitchServerPacket;
import de.pocketcloud.cloudbridge.network.packet.impl.normal.PlayerConnectPacket;
import de.pocketcloud.cloudbridge.network.packet.impl.normal.PlayerDisconnectPacket;
import de.pocketcloud.cloudbridge.network.packet.impl.request.CheckPlayerMaintenanceRequestPacket;
import de.pocketcloud.cloudbridge.network.packet.impl.response.CheckPlayerMaintenanceResponsePacket;
import de.pocketcloud.cloudbridge.network.request.RequestManager;
import dev.waterdog.waterdogpe.event.defaults.PlayerDisconnectEvent;
import dev.waterdog.waterdogpe.event.defaults.PlayerLoginEvent;
import dev.waterdog.waterdogpe.event.defaults.PreTransferEvent;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import dev.waterdog.waterdogpe.utils.config.JsonConfig;

public class EventListener {

    public static void onLogin(PlayerLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        Network.getInstance().sendPacket(new PlayerConnectPacket(new CloudPlayer(player.getName(), player.getAddress().getAddress().getHostAddress() + ":" + player.getAddress().getPort(), player.getXuid(), player.getUniqueId().toString(), null, null)));

        if (CloudAPI.getInstance().getCurrentTemplate() == null) return;
        if (CloudAPI.getInstance().getCurrentTemplate().isMaintenance()) {
            RequestManager.getInstance().sendRequest(new CheckPlayerMaintenanceRequestPacket(player.getName())).then(responsePacket -> {
                CheckPlayerMaintenanceResponsePacket checkPlayerMaintenanceResponsePacket = (CheckPlayerMaintenanceResponsePacket) responsePacket;
                if (!checkPlayerMaintenanceResponsePacket.getValue()) {
                    player.disconnect(new JsonConfig(CloudAPI.getInstance().getCloudPath() + "/storage/inGame/messages.json").getString("template-maintenance"));
                }
            });
        }
    }

    public static void onDisconnect(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        Network.getInstance().sendPacket(new PlayerDisconnectPacket(new CloudPlayer(player.getName(), player.getAddress().getAddress().getHostAddress() + ":" + player.getAddress().getPort(), player.getXuid(), player.getUniqueId().toString(), null, null)));
    }

    public static void onTransfer(PreTransferEvent event) {
        Network.getInstance().sendPacket(new CloudPlayerSwitchServerPacket(event.getPlayer().getName(), event.getTargetServer().getServerName()));
    }
}
