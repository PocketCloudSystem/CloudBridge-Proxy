package de.pocketcloud.cloud.bridge.command.sender;

import dev.waterdog.waterdogpe.ProxyServer;
import dev.waterdog.waterdogpe.command.ConsoleCommandSender;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class CloudCommandSender extends ConsoleCommandSender {

    private final String id;
    @Getter
    private final List<String> cachedMessages = new ArrayList<>();

    public CloudCommandSender(String id, ProxyServer proxy) {
        super(proxy);
        this.id = id;
    }

    @Override
    public void sendMessage(String message) {
        super.sendMessage(message);
        cachedMessages.add(message);
    }

    @Override
    public String getName() {
        return "Cloud-" + this.id;
    }
}