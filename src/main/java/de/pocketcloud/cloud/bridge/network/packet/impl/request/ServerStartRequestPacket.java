package de.pocketcloud.cloud.bridge.network.packet.impl.request;

import de.pocketcloud.cloud.bridge.network.packet.RequestPacket;
import de.pocketcloud.cloud.bridge.network.packet.util.PacketData;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
final public class ServerStartRequestPacket extends RequestPacket {

    private String template;
    private int count;

    public ServerStartRequestPacket(String template, int count) {
        this.template = template;
        this.count = count;
    }

    @Override
    public void encodePayload(PacketData packetData) {
        packetData.writeAll(template, count);
    }

    public static ServerStartRequestPacket create(String template, int count) {
        return new ServerStartRequestPacket(template, count);
    }
}