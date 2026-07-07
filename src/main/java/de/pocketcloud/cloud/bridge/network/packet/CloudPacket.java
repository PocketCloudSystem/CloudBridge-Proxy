package de.pocketcloud.cloud.bridge.network.packet;

import de.pocketcloud.cloud.bridge.CloudBridge;
import de.pocketcloud.network.packet.CloudboundPacket;
import de.pocketcloud.network.packet.Packet;
import de.pocketcloud.network.packet.data.PacketData;
import io.netty.channel.Channel;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

public abstract class CloudPacket implements Packet {
    
    private boolean encoded = false;
    private Long sentTimestamp = null;
    @Setter
    @Getter
    private long size = 0;

    @Override
    public void encode(PacketData packetData) {
        if (encoded) throw new RuntimeException("Packet " + getName() + " has already been encoded");
        encoded = true;
        packetData.write(getName()).write(sentTimestamp = System.currentTimeMillis());
        encodePayload(packetData);
    }
    
    @Override
    public void decode(PacketData packetData) {
        String packetName = packetData.readString();
        assert packetName != null;
        if (!packetName.equals(getName())) throw new RuntimeException("Packet name does not equal the actual class name? What have you done?");
        sentTimestamp = packetData.readLong();
        if (sentTimestamp == null) throw new RuntimeException("Packet data does not contain the actual sent timestamp? What have you done?");
        decodePayload(packetData);
    }

    public void sendPacket() {
        if (!(this instanceof CloudboundPacket p)) return;
        CloudBridge.getInstance().getNetwork().sendPacket(p);
    }

    @Override
    public void handle(@NotNull Channel channel) {
        handle();
    }

    public abstract void handle();
    
    @Override
    public final String getName() {
        return getClass().getSimpleName();
    }
    
    @Override
    public final boolean isEncoded() {
        return encoded;
    }
    
    @Override
    public final long getSentTimestamp() {
        return sentTimestamp;
    }
}