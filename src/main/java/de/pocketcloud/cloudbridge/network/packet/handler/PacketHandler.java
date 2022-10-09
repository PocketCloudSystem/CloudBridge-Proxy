package de.pocketcloud.cloudbridge.network.packet.handler;

import de.pocketcloud.cloudbridge.CloudBridge;
import de.pocketcloud.cloudbridge.api.player.CloudPlayer;
import de.pocketcloud.cloudbridge.api.registry.Registry;
import de.pocketcloud.cloudbridge.api.server.CloudServer;
import de.pocketcloud.cloudbridge.api.template.Template;
import de.pocketcloud.cloudbridge.event.NetworkPacketReceiveEvent;
import de.pocketcloud.cloudbridge.network.Network;
import de.pocketcloud.cloudbridge.network.packet.CloudPacket;
import de.pocketcloud.cloudbridge.network.packet.ResponsePacket;
import de.pocketcloud.cloudbridge.network.packet.handler.decode.PacketDecoder;
import de.pocketcloud.cloudbridge.network.packet.impl.normal.*;
import de.pocketcloud.cloudbridge.network.packet.impl.types.DisconnectReason;
import de.pocketcloud.cloudbridge.network.packet.listener.PacketListener;
import de.pocketcloud.cloudbridge.network.request.RequestManager;
import de.pocketcloud.cloudbridge.utils.Utils;
import dev.waterdog.waterdogpe.ProxyServer;
import dev.waterdog.waterdogpe.command.ConsoleCommandSender;
import dev.waterdog.waterdogpe.logger.MainLogger;
import dev.waterdog.waterdogpe.network.serverinfo.BedrockServerInfo;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;

import java.net.InetSocketAddress;

public class PacketHandler {

    private static PacketHandler instance;

    public PacketHandler() {
        instance = this;
        PacketListener.getInstance().register(DisconnectPacket.class, packet -> {
            DisconnectPacket disconnectPacket = (DisconnectPacket) packet;
            if (disconnectPacket.getDisconnectReason() == DisconnectReason.CLOUD_SHUTDOWN) {
                MainLogger.getLogger().emergency("§4Cloud was stopped! Shutdown...");
                ProxyServer.getInstance().shutdown();
            } else {
                MainLogger.getLogger().emergency("§4Server shutdown was ordered by the cloud! Shutdown...");
                ProxyServer.getInstance().shutdown();
            }
        });

        PacketListener.getInstance().register(KeepALivePacket.class, packet -> {
            CloudBridge.getInstance().lastKeepALiveCheck = Utils.microtime();
            Network.getInstance().sendPacket(new KeepALivePacket());
        });

        PacketListener.getInstance().register(CommandSendPacket.class, packet -> ProxyServer.getInstance().dispatchCommand(new ConsoleCommandSender(ProxyServer.getInstance()), ((CommandSendPacket) packet).getCommandLine()));

        PacketListener.getInstance().register(LocalPlayerRegisterPacket.class, packet -> {
            try {
                LocalPlayerRegisterPacket localPlayerRegisterPacket = (LocalPlayerRegisterPacket) packet;
                CloudPlayer player;
                if ((player = CloudPlayer.fromArray(localPlayerRegisterPacket.getPlayer())) != null) Registry.registerPlayer(player);
            } catch (Exception e) {}
         });

        PacketListener.getInstance().register(LocalPlayerUpdatePacket.class, packet -> Registry.updatePlayer(((LocalPlayerUpdatePacket) packet).getPlayer(), ((LocalPlayerUpdatePacket) packet).getNewServer()));
        PacketListener.getInstance().register(LocalPlayerUnregisterPacket.class, packet -> Registry.unregisterPlayer(((LocalPlayerUnregisterPacket) packet).getPlayer()));

        PacketListener.getInstance().register(LocalTemplateRegisterPacket.class, packet -> {
            try {
                LocalTemplateRegisterPacket localTemplateRegisterPacket = (LocalTemplateRegisterPacket) packet;
                Template template;
                if ((template = Template.fromArray(localTemplateRegisterPacket.getTemplate())) != null) Registry.registerTemplate(template);
            } catch (Exception e) {}
        });

        PacketListener.getInstance().register(LocalTemplateUpdatePacket.class, packet -> Registry.updateTemplate(((LocalTemplateUpdatePacket) packet).getTemplate(), ((LocalTemplateUpdatePacket) packet).getNewData()));
        PacketListener.getInstance().register(LocalTemplateUnregisterPacket.class, packet -> Registry.unregisterTemplate(((LocalTemplateUnregisterPacket) packet).getTemplate()));

        PacketListener.getInstance().register(LocalServerRegisterPacket.class, packet -> {
            try {
                LocalServerRegisterPacket localServerRegisterPacket = (LocalServerRegisterPacket) packet;
                CloudServer server;
                if ((server = CloudServer.fromArray(localServerRegisterPacket.getServer())) != null) Registry.registerServer(server);
            } catch (Exception e) {}
        });

        PacketListener.getInstance().register(LocalServerUpdatePacket.class, packet -> Registry.updateServer(((LocalServerUpdatePacket) packet).getServer(), ((LocalServerUpdatePacket) packet).getNewStatus()));
        PacketListener.getInstance().register(LocalServerUnregisterPacket.class, packet -> Registry.unregisterServer(((LocalServerUnregisterPacket) packet).getServer()));

        PacketListener.getInstance().register(ProxyRegisterServerPacket.class, packet -> ProxyServer.getInstance().registerServerInfo(new BedrockServerInfo(((ProxyRegisterServerPacket) packet).getServerName(), new InetSocketAddress("127.0.0.1", ((ProxyRegisterServerPacket) packet).getPort()), null)));
        PacketListener.getInstance().register(ProxyUnregisterServerPacket.class, packet -> ProxyServer.getInstance().removeServerInfo(((ProxyUnregisterServerPacket) packet).getServerName()));

        PacketListener.getInstance().register(PlayerKickPacket.class, packet -> {
            PlayerKickPacket playerKickPacket = (PlayerKickPacket) packet;
            ProxiedPlayer player;
            if ((player = ProxyServer.getInstance().getPlayer(playerKickPacket.getPlayer())) != null) player.disconnect(playerKickPacket.getReason());
        });
    }

    public void handle(String buffer) {
        CloudPacket packet;
        if ((packet = PacketDecoder.decode(buffer)) != null) {
            NetworkPacketReceiveEvent ev = new NetworkPacketReceiveEvent(packet);
            ProxyServer.getInstance().getEventManager().callEvent(ev);

            if (ev.isCancelled()) return;

            PacketListener.getInstance().call(packet);
            if (packet instanceof ResponsePacket) {
                RequestManager.getInstance().callThen((ResponsePacket) packet);
                RequestManager.getInstance().removeRequest(((ResponsePacket) packet).getRequestId());
            }
        }
    }

    public static PacketHandler getInstance() {
        return instance;
    }
}
