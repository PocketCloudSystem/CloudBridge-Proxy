package de.pocketcloud.cloudbridge.network.packet;

import de.pocketcloud.cloudbridge.network.packet.utils.PacketData;
import de.pocketcloud.cloudbridge.util.Utils;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.UUID;
import java.util.function.Consumer;

@NoArgsConstructor
public class RequestPacket extends CloudPacket {

    private String requestId;
    private double sentTime;
    private final ArrayList<Consumer<ResponsePacket>> then = new ArrayList<>();
    private Consumer<RequestPacket> failure = null;

    public void prepare() {
        requestId = UUID.randomUUID().toString();
        sentTime = Utils.time();
    }

    @Override
    public void encode(PacketData packetData) {
        super.encode(packetData);
        packetData.write(requestId);
    }

    @Override
    public void decode(PacketData packetData) {
        super.decode(packetData);
        requestId = packetData.readString();
    }

    public RequestPacket then(Consumer<ResponsePacket> then) {
        this.then.add(then);
        return this;
    }

    public void failure(Consumer<RequestPacket> failure) {
        this.failure = failure;
    }

    final public void handle() {}

    public String getRequestId() {
        return requestId;
    }

    public double getSentTime() {
        return sentTime;
    }

    public ArrayList<Consumer<ResponsePacket>> getThen() {
        return then;
    }

    public Consumer<RequestPacket> getFailure() {
        return failure;
    }
}
