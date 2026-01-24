package de.pocketcloud.cloud.bridge.network.packet.impl;

import de.pocketcloud.cloud.bridge.network.packet.ClientboundPacket;
import de.pocketcloud.cloud.bridge.network.packet.CloudPacket;
import de.pocketcloud.cloud.bridge.network.packet.util.PacketData;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public final class LibrarySyncPacket extends CloudPacket implements ClientboundPacket {
    
    private List<Object> data;
    
    public LibrarySyncPacket(List<Object> data) {
        this.data = data;
    }
    
    @Override
    public void handle() {}
    
    @Override
    public void decodePayload(PacketData packetData) {
        data = packetData.readArray();
        if (data == null) {
            data = new ArrayList<>();
        }
    }

    public static LibrarySyncPacket create(List<Object> data) {
        return new LibrarySyncPacket(data);
    }
}