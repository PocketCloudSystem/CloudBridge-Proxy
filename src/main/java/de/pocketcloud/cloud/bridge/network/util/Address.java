package de.pocketcloud.cloud.bridge.network.util;

import java.net.InetSocketAddress;

public record Address(String address, int port) {

    public static Address create(String address, int port) {
        return new Address(address, port);
    }

    @Override
    public String toString() {
        return address + ":" + port;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Address other)) return false;
        return port == other.port && address.equals(other.address);
    }

    public InetSocketAddress toInetSocketAddress() {
        return new InetSocketAddress(address, port);
    }
}