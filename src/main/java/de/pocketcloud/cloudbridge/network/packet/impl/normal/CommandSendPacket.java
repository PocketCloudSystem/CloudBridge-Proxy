package de.pocketcloud.cloudbridge.network.packet.impl.normal;

import de.pocketcloud.cloudbridge.network.packet.CloudPacket;
import de.pocketcloud.cloudbridge.network.packet.content.PacketContent;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CommandSendPacket extends CloudPacket {

    private String commandLine;

    public CommandSendPacket(String commandLine) {
        this.commandLine = commandLine;
    }

    @Override
    protected void encodePayload(PacketContent content) {
        super.encodePayload(content);
        content.put(commandLine);
    }

    @Override
    protected void decodePayload(PacketContent content) {
        super.decodePayload(content);
        commandLine = content.readString();
    }

    public String getCommandLine() {
        return commandLine;
    }
}
