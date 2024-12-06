package de.pocketcloud.cloudbridge.network.packet.impl.normal;

import de.pocketcloud.cloudbridge.language.Language;
import de.pocketcloud.cloudbridge.network.packet.CloudPacket;
import de.pocketcloud.cloudbridge.network.packet.utils.PacketData;

import java.util.Map;

public class LanguageSyncPacket extends CloudPacket {

    private Map<String, Map<String, String>> data;

    @Override
    protected void encodePayload(PacketData packetData) {
        packetData.write(data);
    }

    @Override
    protected void decodePayload(PacketData packetData) {
        data = (Map<String, Map<String, String>>) packetData.readMap();
    }

    public Map<String, Map<String, String>> getData() {
        return data;
    }

    @Override
    public void handle() {
        data.forEach((key, value) -> {
            Language language;
            if ((language = Language.get(key)) != null) language.sync(value);
        });
    }
}
