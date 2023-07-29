package de.pocketcloud.cloudbridge.network.packet.impl.normal;

import de.pocketcloud.cloudbridge.network.Network;
import de.pocketcloud.cloudbridge.network.packet.CloudPacket;
import de.pocketcloud.cloudbridge.network.packet.impl.types.CommandExecutionResult;
import de.pocketcloud.cloudbridge.network.packet.utils.PacketData;
import de.pocketcloud.cloudbridge.util.CloudCommandSender;
import dev.waterdog.waterdogpe.ProxyServer;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CommandSendPacket extends CloudPacket {

    private String commandLine;

    public CommandSendPacket(String commandLine) {
        this.commandLine = commandLine;
    }

    @Override
    protected void encodePayload(PacketData packetData) {
        super.encodePayload(packetData);
        packetData.write(commandLine);
    }

    @Override
    protected void decodePayload(PacketData packetData) {
        super.decodePayload(packetData);
        commandLine = packetData.readString();
    }

    public String getCommandLine() {
        return commandLine;
    }

    @Override
    public void handle() {
        CloudCommandSender cloudCommandSender = new CloudCommandSender();
        ProxyServer.getInstance().dispatchCommand(cloudCommandSender, commandLine);
        Network.getInstance().sendPacket(new CommandSendAnswerPacket(
                new CommandExecutionResult(commandLine, cloudCommandSender.getCachedMessages())
        ));
    }
}
