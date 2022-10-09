package de.pocketcloud.cloudbridge.handler;

import de.pocketcloud.cloudbridge.api.CloudAPI;
import de.pocketcloud.cloudbridge.api.server.CloudServer;
import dev.waterdog.waterdogpe.ProxyServer;
import dev.waterdog.waterdogpe.network.serverinfo.ServerInfo;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import dev.waterdog.waterdogpe.utils.types.IReconnectHandler;

public class ReconnectHandler implements IReconnectHandler {

    @Override
    public ServerInfo getFallbackServer(ProxiedPlayer player, ServerInfo oldServer, String kickMessage) {
        CloudServer lobby = CloudAPI.getInstance().getFreeLobby(oldServer.getServerName());
        if (lobby == null) return null;
        return ProxyServer.getInstance().getServerInfo(lobby.getName());
    }
}
