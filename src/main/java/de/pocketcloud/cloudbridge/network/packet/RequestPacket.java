package de.pocketcloud.cloudbridge.network.packet;

import de.pocketcloud.cloudbridge.network.packet.content.PacketContent;
import de.pocketcloud.cloudbridge.utils.Utils;
import java.util.UUID;
import java.util.function.Consumer;

public class RequestPacket extends CloudPacket {

    private String requestId;
    private final double sentTime;
    private Consumer<ResponsePacket> then = null;
    private Consumer<RequestPacket> failure = null;

    public RequestPacket() {
        requestId = UUID.randomUUID().toString();
        sentTime = Utils.microtime();
    }

    @Override
    public void encode(PacketContent content) {
        super.encode(content);
        content.put(requestId);
    }

    @Override
    public void decode(PacketContent content) {
        super.decode(content);
        requestId = content.readString();
    }

    public RequestPacket then(Consumer<ResponsePacket> then) {
        this.then = then;
        return this;
    }

    public RequestPacket failure(Consumer<RequestPacket> failure) {
        this.failure = failure;
        return this;
    }

    public String getRequestId() {
        return requestId;
    }

    public double getSentTime() {
        return sentTime;
    }

    public Consumer<ResponsePacket> getThen() {
        return then;
    }

    public Consumer<RequestPacket> getFailure() {
        return failure;
    }
}
