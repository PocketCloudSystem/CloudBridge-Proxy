package de.pocketcloud.cloud.bridge.network.packet.impl;

import de.pocketcloud.cloud.bridge.CloudBridge;
import de.pocketcloud.cloud.bridge.network.packet.ClientboundPacket;
import de.pocketcloud.cloud.bridge.network.packet.CloudboundPacket;
import de.pocketcloud.cloud.bridge.network.packet.CloudPacket;
import de.pocketcloud.cloud.bridge.network.packet.util.PacketData;

public final class KeepAlivePacket extends CloudPacket implements ClientboundPacket, CloudboundPacket {
    
    @Override
    public void handle() {
        CloudBridge.getInstance().setLastKeepAliveCheck((int) (System.currentTimeMillis() / 1000));
        create().sendPacket();
    }
    
    @Override
    public void encodePayload(PacketData packetData) {}
    
    @Override
    public void decodePayload(PacketData packetData) {}
    
    public static KeepAlivePacket create() {
        return new KeepAlivePacket();
    }
}