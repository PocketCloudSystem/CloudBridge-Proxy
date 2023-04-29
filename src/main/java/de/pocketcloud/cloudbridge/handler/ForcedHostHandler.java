package de.pocketcloud.cloudbridge.handler;

import de.pocketcloud.cloudbridge.api.CloudAPI;
import de.pocketcloud.cloudbridge.api.server.CloudServer;
import dev.waterdog.waterdogpe.ProxyServer;
import dev.waterdog.waterdogpe.network.connection.handler.IForcedHostHandler;
import dev.waterdog.waterdogpe.network.serverinfo.ServerInfo;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;

public class ForcedHostHandler implements IForcedHostHandler {

    @Override
    public ServerInfo resolveForcedHost(String s, ProxiedPlayer proxiedPlayer) {
        CloudServer lobby = CloudAPI.getInstance().getFreeLobby();
        if (lobby == null) return null;
        return ProxyServer.getInstance().getServerInfo(lobby.getName());
    }
}
