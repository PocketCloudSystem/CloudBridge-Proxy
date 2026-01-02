package de.pocketcloud.cloud.bridge.network.packet;

import de.pocketcloud.cloud.bridge.network.exception.PacketException;
import de.pocketcloud.cloud.bridge.network.packet.util.PacketSerializer;
import de.pocketcloud.cloud.bridge.network.util.Address;

public record UnhandledPacket(byte[] buffer, Address address, int bytes) {

    public ClientboundPacket buildCloudPacket(boolean encryptionEnabled, String authKey) throws PacketException {
        return PacketSerializer.decode(buffer, encryptionEnabled, authKey);
    }
}