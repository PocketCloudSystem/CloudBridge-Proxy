package de.pocketcloud.cloudbridge.network.packet.impl.normal;

import de.pocketcloud.cloudbridge.network.packet.CloudPacket;
import de.pocketcloud.cloudbridge.network.packet.impl.types.CommandExecutionResult;
import de.pocketcloud.cloudbridge.network.packet.utils.PacketData;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CommandSendAnswerPacket extends CloudPacket {

    private CommandExecutionResult result;

    public CommandSendAnswerPacket(CommandExecutionResult commandExecutionResult) {
        this.result = commandExecutionResult;
    }

    @Override
    protected void encodePayload(PacketData packetData) {
        super.encodePayload(packetData);
        packetData.writeCommandExecutionResult(result);
    }

    @Override
    protected void decodePayload(PacketData packetData) {
        super.decodePayload(packetData);
        result = packetData.readCommandExecutionResult();
    }

    public CommandExecutionResult getResult() {
        return result;
    }

    @Override
    public void handle() {}
}
