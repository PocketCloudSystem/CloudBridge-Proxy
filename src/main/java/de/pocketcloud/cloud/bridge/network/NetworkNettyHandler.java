package de.pocketcloud.cloud.bridge.network;

import de.pocketcloud.cloud.bridge.CloudBridge;
import de.pocketcloud.cloud.bridge.network.packet.CloudPacket;
import de.pocketcloud.cloud.bridge.network.packet.ResponsePacket;
import de.pocketcloud.cloud.bridge.network.request.RequestManager;
import de.pocketcloud.network.traffic.TrafficDirection;
import de.pocketcloud.network.traffic.TrafficMonitorManager;
import de.pocketcloud.network.traffic.impl.NetworkTrafficMonitor;
import dev.waterdog.waterdogpe.ProxyServer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.SocketException;

public class NetworkNettyHandler extends SimpleChannelInboundHandler<CloudPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CloudPacket packet) {
        try {
            TrafficMonitorManager.instance().callHandlers(NetworkTrafficMonitor.class, NetworkTrafficMonitor.parsePacketMode(TrafficDirection.IN, packet.getClass()), ctx.channel(), packet, packet.getSize());
            if (packet instanceof ResponsePacket responsePacket) {
                RequestManager.getInstance().resolve(responsePacket);
                RequestManager.getInstance().remove(responsePacket.getRequestId());
                return;
            }

            packet.handle(ctx.channel());
        } catch (Exception e) {
            CloudBridge.getInstance().getLogger().error("Unhandled exception while processing packet §b{} §rsent by §b{}§r. §8(§renable §edebug §rto view full stack trace§8)", packet.getName(), ctx.channel().remoteAddress());
            if (ProxyServer.getInstance().getConfiguration().isDebug()) CloudBridge.getInstance().getLogger().error("Exception:", e);
            else CloudBridge.getInstance().getLogger().error(e.getMessage());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        if (cause instanceof SocketException) {
            if (cause.getMessage().equals("Connection reset")) {
                ProxyServer.getInstance().shutdown();
                return;
            }
        }

        CloudBridge.getInstance().getLogger().error("Unhandled exception caused by §b{}§r. §8(§renable §edebug §rto view full stack trace§8)", ctx.channel().remoteAddress());
        if (ProxyServer.getInstance().getConfiguration().isDebug()) CloudBridge.getInstance().getLogger().error("Exception:", cause);
        else CloudBridge.getInstance().getLogger().error(cause.getMessage());
    }
}