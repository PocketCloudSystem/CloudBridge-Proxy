package de.pocketcloud.cloudbridge.network.packet.handler.encode;

import com.google.gson.Gson;
import de.pocketcloud.cloudbridge.network.packet.CloudPacket;
import de.pocketcloud.cloudbridge.network.packet.content.PacketContent;

public class PacketEncoder {

    public static String encode(CloudPacket packet) {
        PacketContent content = new PacketContent();
        packet.encode(content);
        return new Gson().toJson(content.getContent());
    }
}
