package de.pocketcloud.cloud.bridge.handler;

import de.pocketcloud.cloud.bridge.api.CloudAPI;
import de.pocketcloud.cloud.bridge.api.object.server.CloudServer;
import de.pocketcloud.cloud.bridge.api.provider.CloudServerProvider;
import de.pocketcloud.cloud.bridge.network.packet.data.VerifyStatus;
import dev.waterdog.waterdogpe.ProxyServer;
import dev.waterdog.waterdogpe.network.connection.handler.IJoinHandler;
import dev.waterdog.waterdogpe.network.connection.handler.IReconnectHandler;
import dev.waterdog.waterdogpe.network.connection.handler.ReconnectReason;
import dev.waterdog.waterdogpe.network.serverinfo.BedrockServerInfo;
import dev.waterdog.waterdogpe.network.serverinfo.ServerInfo;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;

final public class ConnectAndReconnectHandler implements IReconnectHandler, IJoinHandler {

    public ServerInfo getFallbackServer(ProxiedPlayer player, ServerInfo oldServer, ReconnectReason reason, String kickMessage) {
        if (reason != ReconnectReason.UNKNOWN) {
            if (reason == ReconnectReason.SERVER_KICK) {
                if (kickMessage != null) {
                    return (kickMessage.contains("closed") || kickMessage.contains("shutdown")) ? anyLobbyServer(oldServer) : null;
                }
                return null;
            }
            return anyLobbyServer(oldServer);
        }
        return null;
    }

    @Override
    public ServerInfo determineServer(ProxiedPlayer proxiedPlayer) {
        return anyLobbyServer(null);
    }

    private BedrockServerInfo anyLobbyServer(ServerInfo oldServer) {
        if (CloudAPI.get().getVerifyStatus() == VerifyStatus.VERIFIED) {
            List<CloudServer> lobbyServers = new ArrayList<>(CloudServerProvider.provider().lobbyServers());
            if (oldServer != null) {
                if (lobbyServers.stream().anyMatch(server -> server.getName().equals(oldServer.getServerName())))
                    lobbyServers.remove(lobbyServers.stream().filter(server -> server.getName().equals(oldServer.getServerName())).findFirst().orElse(null));
            }

            CloudServer cloudServer = lobbyServers.stream().findAny().orElse(null);
            if (cloudServer != null) {
                return ProxyServer.getInstance().getServerInfo(cloudServer.getName(), BedrockServerInfo.class);
            }
        }

        return null;
    }
}