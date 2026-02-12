package de.pocketcloud.cloud.bridge.network.packet.impl;

import de.pocketcloud.cloud.bridge.api.object.server.util.ServerStatus;
import de.pocketcloud.cloud.bridge.network.packet.CloudPacket;
import de.pocketcloud.cloud.bridge.network.packet.CloudboundPacket;
import de.pocketcloud.cloud.bridge.network.packet.util.PacketData;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public final class ServerChangeStatusPacket extends CloudPacket implements CloudboundPacket {

    private String serverUuid;
    private ServerStatus status;

    public ServerChangeStatusPacket(String serverUuid, ServerStatus status) {
        this.serverUuid = serverUuid;
        this.status = status;
    }

    @Override
    public void handle() {}

    @Override
    public void encodePayload(PacketData packetData) {
        packetData.writeAll(serverUuid, status);
    }

    public static ServerChangeStatusPacket create(String serverUuid, ServerStatus status) {
        return new ServerChangeStatusPacket(serverUuid, status);
    }
}
