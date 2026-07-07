package de.pocketcloud.cloud.bridge.network.packet.impl.response.client;

import de.pocketcloud.network.packet.CloudboundPacket;
import de.pocketcloud.cloud.bridge.network.packet.ResponseClientPacket;
import de.pocketcloud.cloud.bridge.network.packet.type.ServerCommandExecutionResult;
import de.pocketcloud.network.packet.data.PacketData;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public final class CommandExecuteResponsePacket extends ResponseClientPacket implements CloudboundPacket {
    
    private ServerCommandExecutionResult commandExecutionResult;

    public CommandExecuteResponsePacket(ServerCommandExecutionResult commandExecutionResult) {
        this.commandExecutionResult = commandExecutionResult;
    }

    @Override
    public void encodePayload(PacketData packetData) {
        packetData.writeAll(commandExecutionResult);
    }

    public static CommandExecuteResponsePacket create(ServerCommandExecutionResult commandExecutionResult) {
        return new CommandExecuteResponsePacket(commandExecutionResult);
    }
}