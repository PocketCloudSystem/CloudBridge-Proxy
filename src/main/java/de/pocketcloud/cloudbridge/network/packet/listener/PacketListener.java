package de.pocketcloud.cloudbridge.network.packet.listener;

import de.pocketcloud.cloudbridge.network.packet.CloudPacket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

public class PacketListener {

    private static PacketListener instance;
    private final HashMap<String, ArrayList<Consumer<CloudPacket>>> handlers = new HashMap<>();

    public PacketListener() {
        instance = this;
    }

    public void register(Class<? extends CloudPacket> packetClass, Consumer<CloudPacket> consumer) {
        if (!handlers.containsKey(packetClass.getSimpleName())) handlers.put(packetClass.getSimpleName(), new ArrayList<>());
        handlers.get(packetClass.getSimpleName()).add(consumer);
    }

    public void call(CloudPacket packet) {
        if (!handlers.containsKey(packet.getClass().getSimpleName())) return;
        for (Consumer<CloudPacket> consumer : handlers.get(packet.getClass().getSimpleName())) {
            consumer.accept(packet);
        }
    }

    public static PacketListener getInstance() {
        return instance;
    }
}
