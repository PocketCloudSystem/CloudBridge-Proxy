package de.pocketcloud.cloudbridge.handler;

import de.pocketcloud.cloudbridge.api.CloudAPI;
import de.pocketcloud.cloudbridge.api.server.CloudServer;
import dev.waterdog.waterdogpe.ProxyServer;
import dev.waterdog.waterdogpe.network.serverinfo.ServerInfo;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import dev.waterdog.waterdogpe.utils.types.IJoinHandler;

public class JoinHandler implements IJoinHandler {

    @Override
    public ServerInfo determineServer(ProxiedPlayer player) {
        CloudServer lobby = CloudAPI.getInstance().getFreeLobby();
        if (lobby == null) return null;
        return ProxyServer.getInstance().getServerInfo(lobby.getName());
    }
}
