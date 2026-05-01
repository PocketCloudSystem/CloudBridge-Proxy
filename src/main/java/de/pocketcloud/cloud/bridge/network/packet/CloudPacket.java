package de.pocketcloud.cloud.bridge.network.packet;

import de.pocketcloud.cloud.bridge.network.Network;
import de.pocketcloud.cloud.bridge.network.packet.util.PacketData;
import de.pocketcloud.cloud.bridge.util.Utils;

public abstract class CloudPacket implements Packet {
    
    private boolean encoded = false;
    private Double sentTimestamp = null;
    
    @Override
    public void encode(PacketData packetData) {
        if (encoded) throw new RuntimeException("Packet " + getName() + " has already been encoded");
        encoded = true;
        packetData.write(getName()).write(sentTimestamp = Utils.time());
        encodePayload(packetData);
    }
    
    @Override
    public void decode(PacketData packetData) {
        String packetName = packetData.readString();
        assert packetName != null;
        if (!packetName.equals(getName())) throw new RuntimeException("Packet name does not equal the actual class name? What have you done?");
        sentTimestamp = packetData.readDouble();
        if (sentTimestamp == null) throw new RuntimeException("Packet data does not contain the actual sent timestamp? What have you done?");
        decodePayload(packetData);
    }

    public void sendPacket() {
        if (!(this instanceof CloudboundPacket)) return;
        Network.getInstance().sendPacket((CloudboundPacket) this);
    }
    
    @Override
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
    public final Double getSentTimestamp() {
        return sentTimestamp;
    }
}