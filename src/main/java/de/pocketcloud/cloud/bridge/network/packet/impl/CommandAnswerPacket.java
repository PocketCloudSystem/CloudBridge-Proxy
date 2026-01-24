package de.pocketcloud.cloud.bridge.network.packet.impl;

import de.pocketcloud.cloud.bridge.network.packet.CloudboundPacket;
import de.pocketcloud.cloud.bridge.network.packet.CloudPacket;
import de.pocketcloud.cloud.bridge.network.packet.data.ServerCommandExecutionResult;
import de.pocketcloud.cloud.bridge.network.packet.util.PacketData;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public final class CommandAnswerPacket extends CloudPacket implements CloudboundPacket {
    
    private ServerCommandExecutionResult commandExecutionResult;

    public CommandAnswerPacket(ServerCommandExecutionResult commandExecutionResult) {
        this.commandExecutionResult = commandExecutionResult;
    }

    @Override
    public void handle() {}

    @Override
    public void encodePayload(PacketData packetData) {
        packetData.writeAll(commandExecutionResult);
    }

    public static CommandAnswerPacket create(ServerCommandExecutionResult commandExecutionResult) {
        return new CommandAnswerPacket(commandExecutionResult);
    }
}