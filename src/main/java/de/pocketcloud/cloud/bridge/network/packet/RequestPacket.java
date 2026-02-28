package de.pocketcloud.cloud.bridge.network.packet;

import de.pocketcloud.cloud.bridge.network.packet.util.PacketData;
import de.pocketcloud.cloud.bridge.network.request.RequestManager;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.BiConsumer;

/**
 * The normal request packet sent from sub-servers to the cloud, which will answer through regular ResponsePacket
 * @see ResponsePacket
 */
public abstract class RequestPacket extends CloudPacket implements CloudboundPacket {
    
    @Getter
    private String requestId = null;
    private final List<BiFunction<ResponsePacket, Object, Object>> thenCallbacks = new ArrayList<>();
    private BiConsumer<RequestPacket, Exception> failureCallback = null;

    public void prepare() {
        if (requestId != null) return;
        requestId = UUID.randomUUID().toString();
    }
    
    @Override
    final public void encode(PacketData packetData) {
        super.encode(packetData);
        packetData.write(requestId);
    }
    
    @Override
    final public void decode(PacketData packetData) {
        super.decode(packetData);
        requestId = packetData.readString();
    }

    @Override
    final public boolean sendPacket() {
        throw new RuntimeException("Use sendRequest() instead of sendPacket()");
    }

    public RequestPacket sendRequest() {
        return RequestManager.getInstance().send(this);
    }

    public final void invokeCallbacks(boolean failed, ResponsePacket responsePacket, Exception exception) {
        if (failed) {
            if (failureCallback != null) {
                failureCallback.accept(this, exception);
            }
            return;
        }
        
        Object value = null;
        
        try {
            for (BiFunction<ResponsePacket, Object, Object> callback : thenCallbacks) {
                value = callback.apply(responsePacket, value);
            }
        } catch (Exception e) {
            if (failureCallback != null) {
                failureCallback.accept(this, e);
            }
        }
    }

    public RequestPacket then(BiFunction<ResponsePacket, Object, Object> callback) {
        thenCallbacks.add(callback);
        return this;
    }

    public RequestPacket failure(BiConsumer<RequestPacket, Exception> callback) {
        this.failureCallback = callback;
        return this;
    }
    
    public boolean isPrepared() {
        return requestId != null;
    }

    @Override
    public final void handle() {}
}