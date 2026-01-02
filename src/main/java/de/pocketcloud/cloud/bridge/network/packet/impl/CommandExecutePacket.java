package de.pocketcloud.cloud.bridge.network.packet.impl;

import de.pocketcloud.cloud.bridge.command.sender.CloudCommandSender;
import de.pocketcloud.cloud.bridge.network.packet.ClientboundPacket;
import de.pocketcloud.cloud.bridge.network.packet.CloudPacket;
import de.pocketcloud.cloud.bridge.network.packet.data.ServerCommandExecutionResult;
import de.pocketcloud.cloud.bridge.network.packet.util.PacketData;
import dev.waterdog.waterdogpe.ProxyServer;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public final class CommandExecutePacket extends CloudPacket implements ClientboundPacket {
    
    private String commandLine;
    private String id;
    
    public CommandExecutePacket(String commandLine, String id) {
        this.commandLine = commandLine;
        this.id = id;
    }
    
    @Override
    public void handle() {
        CloudCommandSender sender = new CloudCommandSender(id, ProxyServer.getInstance());
        ProxyServer.getInstance().dispatchCommand(sender, commandLine);
        CommandAnswerPacket.create(new ServerCommandExecutionResult(id, commandLine, sender.getCachedMessages())).sendPacket();
    }
    
    @Override
    public void encodePayload(PacketData packetData) {}
    
    @Override
    public void decodePayload(PacketData packetData) {
        commandLine = packetData.readString();
        id = packetData.readString();
    }

    public static CommandExecutePacket create(String commandLine, String id) {
        return new CommandExecutePacket(commandLine, id);
    }
}