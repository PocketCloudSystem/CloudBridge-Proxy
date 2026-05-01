package de.pocketcloud.cloud.bridge.exception;

import de.pocketcloud.cloud.bridge.network.packet.CloudboundPacket;

public class PacketTooLargeException extends PacketException {

    public PacketTooLargeException(CloudboundPacket packet, long length, long limit) {
        super("Packet " + packet.getName() + " is too large: " + length + " > " + limit);
    }
}