package de.pocketcloud.cloud.bridge.network;

import de.pocketcloud.cloud.bridge.CloudBridge;
import de.pocketcloud.cloud.bridge.event.packet.*;
import de.pocketcloud.cloud.bridge.util.CloudEnvironmentConfig;
import de.pocketcloud.cloud.bridge.util.EventCaller;
import de.pocketcloud.network.codec.CloudPacketDecoder;
import de.pocketcloud.network.codec.CloudPacketEncoder;
import de.pocketcloud.network.packet.CloudboundPacket;
import de.pocketcloud.network.packet.Packet;
import de.pocketcloud.network.traffic.PacketTrafficListener;
import de.pocketcloud.network.traffic.TrafficDirection;
import de.pocketcloud.network.traffic.TrafficMonitorManager;
import de.pocketcloud.network.traffic.impl.NetworkTrafficMonitor;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import org.jetbrains.annotations.Nullable;

public class NetworkNettyClientInitializer extends ChannelInitializer<Channel> {

    private static final PacketTrafficListener LISTENER = new PacketTrafficListener() {

        @Override
        public boolean onOutgoing(Channel channel, Packet packet, byte[] payload, int length) {
            TrafficMonitorManager.instance().pushBytes(NetworkTrafficMonitor.class, TrafficDirection.OUT, length);
            TrafficMonitorManager.instance().callHandlers(NetworkTrafficMonitor.class, TrafficDirection.OUT, channel, payload, packet.getSize());

            if (EventCaller.call(new PacketPreSendEvent((CloudboundPacket) packet)).isCancelled()) {
                return false;
            }

            TrafficMonitorManager.instance().callHandlers(NetworkTrafficMonitor.class, NetworkTrafficMonitor.parsePacketMode(TrafficDirection.OUT, packet.getClass()), channel, packet, packet.getSize());
            EventCaller.call(new PacketSentEvent((CloudboundPacket) packet));

            return true;
        }

        @Override
        public boolean onIncoming(Channel channel, byte[] payload, int length) {
            TrafficMonitorManager.instance().pushBytes(NetworkTrafficMonitor.class, TrafficDirection.IN, length);
            TrafficMonitorManager.instance().callHandlers(NetworkTrafficMonitor.class, TrafficDirection.IN, channel, payload, (long) length);

            return !EventCaller.call(new PacketReceivePreProcessEvent(payload, CloudEnvironmentConfig.isNetworkEncryptionEnabled())).isCancelled();
        }

        @Override
        public void onUnknownPacket(Channel channel, byte[] payload, int length) {
            EventCaller.call(new PacketReceiveUnknownEvent(payload, length, CloudEnvironmentConfig.isNetworkEncryptionEnabled()));
            CloudBridge.getInstance().getLogger().debug("Received unknown packet with size {} from {}", length, channel.remoteAddress().toString());
            CloudBridge.getInstance().getLogger().debug(payload);
        }

        @Override
        public void onTooLargePacket(Channel channel, @Nullable Packet packet, int length, TrafficDirection direction) {
            EventCaller.call(new PacketTooLargeEvent(packet, length, direction));
            if (direction.equals(TrafficDirection.IN)) {
                CloudBridge.getInstance().getLogger().debug("Received a way too big packet with size {} from {}", length, channel.remoteAddress().toString());
            } else {
                CloudBridge.getInstance().getLogger().debug("Tried to send a way too big packet with size {} to {}", length, channel.remoteAddress().toString());
            }
        }
    };

    @Override
    protected void initChannel(Channel channel) {
        channel.pipeline().addLast(
            new CloudPacketDecoder(CloudEnvironmentConfig::isNetworkEncryptionEnabled, CloudEnvironmentConfig::getNetworkPacketSizeLimit, CloudEnvironmentConfig::getNetworkAuthKey, LISTENER),
            new CloudPacketEncoder(CloudEnvironmentConfig::isNetworkEncryptionEnabled, CloudEnvironmentConfig::getNetworkPacketSizeLimit, CloudEnvironmentConfig::getNetworkAuthKey, LISTENER),
            new NetworkNettyHandler()
        );
    }
}