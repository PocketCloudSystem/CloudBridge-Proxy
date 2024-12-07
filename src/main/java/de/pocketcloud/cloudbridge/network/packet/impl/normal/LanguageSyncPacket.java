package de.pocketcloud.cloudbridge.network.packet.impl.normal;

import de.pocketcloud.cloudbridge.language.Language;
import de.pocketcloud.cloudbridge.network.packet.CloudPacket;
import de.pocketcloud.cloudbridge.network.packet.data.PacketData;
import org.yaml.snakeyaml.Yaml;

import java.util.Map;

public class LanguageSyncPacket extends CloudPacket {

    private String language;
    private Map<String, String> messages;

    @Override
    protected void encodePayload(PacketData packetData) {
        packetData.write(language);
        packetData.write(messages);
    }

    @Override
    protected void decodePayload(PacketData packetData) {
        language = packetData.readString();
        messages = (Map<String, String>) packetData.readMap();
    }

    public String getLanguage() {
        return language;
    }

    public Map<String, String> getMessages() {
        return messages;
    }

    @Override
    public void handle() {
        Language lang;
        if ((lang = Language.get(language)) != null) lang.sync(messages);
    }
}
