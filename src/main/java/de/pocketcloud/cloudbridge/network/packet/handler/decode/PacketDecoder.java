package de.pocketcloud.cloudbridge.network.packet.handler.decode;

import com.google.gson.Gson;
import de.pocketcloud.cloudbridge.network.packet.CloudPacket;
import de.pocketcloud.cloudbridge.network.packet.content.PacketContent;
import de.pocketcloud.cloudbridge.network.packet.pool.PacketPool;

import java.util.ArrayList;

public class PacketDecoder {

    public static CloudPacket decode(String buffer) {
        ArrayList<Object> contents = (ArrayList<Object>) new Gson().fromJson(buffer, ArrayList.class);
        if (!contents.isEmpty()) {
            CloudPacket packet = PacketPool.getInstance().getPacketById(contents.get(0).toString());
            if (packet != null) {
                packet.decode(new PacketContent(contents));
                return packet;
            }
        }
        return null;
    }
}
