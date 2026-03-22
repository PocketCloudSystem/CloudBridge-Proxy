package de.pocketcloud.cloud.bridge.traffic.impl;

import de.pocketcloud.cloud.bridge.network.packet.CloudPacket;
import de.pocketcloud.cloud.bridge.network.packet.Packet;
import de.pocketcloud.cloud.bridge.network.util.Address;
import de.pocketcloud.cloud.bridge.traffic.TrafficMonitor;
import de.pocketcloud.cloud.bridge.traffic.TrafficMonitorManager;

import java.util.function.BiConsumer;

public final class NetworkTrafficMonitor extends TrafficMonitor {

    public static final String NETWORK_MODE_PACKET_IN = "packet_in";
    public static final String NETWORK_MODE_PACKET_OUT = "packet_out";

    public NetworkTrafficMonitor() {
        super(TrafficMonitorManager.TRAFFIC_NETWORK);
    }

    /**
     * Monitor incoming packets of a specific type.
     *
     * @param packetClass the packet class to listen for
     * @param handler (CloudPacket packet, Address source)
     */
    public <T extends CloudPacket> NetworkTrafficMonitor monitorPacketIn(
            Class<T> packetClass,
            BiConsumer<T, Address> handler
    ) {
        addHandler(parsePacketMode(NETWORK_MODE_PACKET_IN, packetClass), args -> {
            @SuppressWarnings("unchecked")
            T packet = (T) args[0];
            handler.accept(packet, (Address) args[1]);
        });
        return this;
    }

    /**
     * Monitor outgoing packets of a specific type.
     *
     * @param packetClass the packet class to listen for
     * @param handler (CloudPacket packet, Address destination, boolean success)
     */
    public <T extends CloudPacket> NetworkTrafficMonitor monitorPacketOut(
            Class<T> packetClass,
            TriConsumer<T, Address, Boolean> handler
    ) {
        addHandler(parsePacketMode(NETWORK_MODE_PACKET_OUT, packetClass), args -> {
            @SuppressWarnings("unchecked")
            T packet = (T) args[0];
            handler.accept(packet, (Address) args[1], (Boolean) args[2]);
        });
        return this;
    }

    public static String parsePacketMode(String normalMode, Class<? extends Packet> packetClass) {
        return normalMode + "-" + packetClass.getSimpleName();
    }

    /** Minimal tri-consumer functional interface matching the PHP handler signature. */
    @FunctionalInterface
    public interface TriConsumer<A, B, C> {
        void accept(A a, B b, C c);
    }
}