package de.pocketcloud.cloud.bridge.listener;

import de.pocketcloud.cloud.bridge.CloudBridge;
import de.pocketcloud.cloud.bridge.api.cache.MaintenanceListCache;
import de.pocketcloud.cloud.bridge.api.object.player.CloudPlayer;
import de.pocketcloud.cloud.bridge.api.provider.TemplateProvider;
import de.pocketcloud.cloud.bridge.language.LanguageKey;
import de.pocketcloud.cloud.bridge.network.packet.data.NotificationType;
import de.pocketcloud.cloud.bridge.network.packet.impl.PlayerConnectPacket;
import de.pocketcloud.cloud.bridge.network.packet.impl.PlayerDisconnectPacket;
import de.pocketcloud.cloud.bridge.network.packet.impl.PlayerSwitchServerPacket;
import de.pocketcloud.cloud.bridge.util.CloudEnvironmentConfig;
import dev.waterdog.waterdogpe.event.defaults.InitialServerDeterminedEvent;
import dev.waterdog.waterdogpe.event.defaults.PlayerDisconnectedEvent;
import dev.waterdog.waterdogpe.event.defaults.PlayerLoginEvent;
import dev.waterdog.waterdogpe.event.defaults.ServerTransferEvent;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EventListener {

    private static final List<String> initialConnects = new ArrayList<>();

    public static void onLogin(PlayerLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        if (!CloudBridge.getInstance().getAcceptedProtocols().contains(player.getProtocol().getProtocol())) {
            event.setCancelReason("Login failed. (Incompatible Protocol Version)");
            event.setCancelled();
            return;
        }

        if (TemplateProvider.provider().current().isMaintenance() && !MaintenanceListCache.is(player.getName())) {
            event.setCancelReason(LanguageKey.INGAME_TEMPLATE_KICK_MAINTENANCE.translate());
            event.setCancelled();
            return;
        }

        PlayerConnectPacket.create(CloudPlayer.fromProxiedPlayer(player)).sendPacket();
    }

    public static void onDisconnect(PlayerDisconnectedEvent event) {
        ProxiedPlayer player = event.getPlayer();
        PlayerDisconnectPacket.create(player.getName()).sendPacket();

        if (!initialConnects.contains(player.getName())) {
            String reason = (LanguageKey.INGAME_TEMPLATE_KICK_MAINTENANCE.translate().equals(event.getReason()) ? "Template is in maintenance" : event.getReason());
            NotificationType.PLAYER_JOIN_FAILED.sendNotification(Map.of(
                    "player", player.getName(),
                    "server", CloudEnvironmentConfig.getServerName(),
                    "reason", reason
            ));
        } else initialConnects.remove(player.getName());
    }

    public static void onServerSwitch(ServerTransferEvent event) {
        PlayerSwitchServerPacket.create(event.getPlayer().getName(), event.getTargetServer().getServerName()).sendPacket();
    }

    public static void onInitialServerDetermined(InitialServerDeterminedEvent event) {
        initialConnects.add(event.getPlayer().getName());
        PlayerSwitchServerPacket.create(event.getPlayer().getName(), event.getInitialServer().getServerName()).sendPacket();
    }
}