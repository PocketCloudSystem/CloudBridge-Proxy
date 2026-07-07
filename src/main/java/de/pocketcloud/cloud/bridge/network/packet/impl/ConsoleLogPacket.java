package de.pocketcloud.cloud.bridge.network.packet.impl;

import de.pocketcloud.cloud.bridge.CloudBridge;
import de.pocketcloud.network.packet.ClientboundPacket;
import de.pocketcloud.cloud.bridge.network.packet.CloudPacket;
import de.pocketcloud.network.packet.CloudboundPacket;
import de.pocketcloud.cloud.bridge.network.packet.type.LogType;
import de.pocketcloud.network.packet.data.PacketData;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
final public class ConsoleLogPacket extends CloudPacket implements ClientboundPacket, CloudboundPacket {

    private String message;
    private LogType logType;

    public ConsoleLogPacket(String message, LogType logType) {
        this.message = message;
        this.logType = logType;
    }

    @Override
    public void handle() {
        CloudBridge.getInstance().getLogger().log(logType.toLogLevel(), message);
    }

    @Override
    public void encodePayload(PacketData packetData) {
        packetData.writeAll(message, logType);
    }

    @Override
    public void decodePayload(PacketData packetData) {
        message = packetData.readString();
        logType = packetData.readEnum(LogType.class);
    }

    public static ConsoleLogPacket create(String message, LogType logType) {
        return new ConsoleLogPacket(message, logType);
    }
}