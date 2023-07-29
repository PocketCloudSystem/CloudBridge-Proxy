package de.pocketcloud.cloudbridge.network.packet.impl.request;

import de.pocketcloud.cloudbridge.network.packet.RequestPacket;
import de.pocketcloud.cloudbridge.network.packet.utils.PacketData;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CloudServerStartRequestPacket extends RequestPacket {

    private String template;
    private int count;

    public CloudServerStartRequestPacket(String template, int count) {
        this.template = template;
        this.count = count;
    }

    @Override
    protected void encodePayload(PacketData packetData) {
        super.encodePayload(packetData);
        packetData.write(template);
        packetData.write(packetData);
    }

    @Override
    protected void decodePayload(PacketData packetData) {
        super.decodePayload(packetData);
        template = packetData.readString();
        count = packetData.readInt();
    }

    public String getTemplate() {
        return template;
    }

    public int getCount() {
        return count;
    }
}
