package de.pocketcloud.cloud.bridge.network.packet.impl.request.client;

import de.pocketcloud.cloud.bridge.command.sender.CloudCommandSender;
import de.pocketcloud.network.packet.ClientboundPacket;
import de.pocketcloud.cloud.bridge.network.packet.RequestClientPacket;
import de.pocketcloud.cloud.bridge.network.packet.type.ServerCommandExecutionResult;
import de.pocketcloud.cloud.bridge.network.packet.impl.response.client.CommandExecuteResponsePacket;
import de.pocketcloud.network.packet.data.PacketData;
import dev.waterdog.waterdogpe.ProxyServer;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public final class CommandExecuteRequestPacket extends RequestClientPacket implements ClientboundPacket {
    
    private String commandLine;
    private String id;
    
    public CommandExecuteRequestPacket(String commandLine, String id) {
        this.commandLine = commandLine;
        this.id = id;
    }
    
    @Override
    public void handle() {
        CloudCommandSender sender = new CloudCommandSender(id, ProxyServer.getInstance());
        ProxyServer.getInstance().dispatchCommand(sender, commandLine);
        sendResponse(CommandExecuteResponsePacket.create(new ServerCommandExecutionResult(id, commandLine, sender.getCachedMessages())));
    }

    @Override
    public void decodePayload(PacketData packetData) {
        commandLine = packetData.readString();
        id = packetData.readString();
    }

    public static CommandExecuteRequestPacket create(String commandLine, String id) {
        return new CommandExecuteRequestPacket(commandLine, id);
    }
}