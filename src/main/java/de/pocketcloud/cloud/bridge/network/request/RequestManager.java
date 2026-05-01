package de.pocketcloud.cloud.bridge.network.request;

import de.pocketcloud.cloud.bridge.network.Network;
import de.pocketcloud.cloud.bridge.network.packet.RequestPacket;
import de.pocketcloud.cloud.bridge.network.packet.ResponsePacket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class RequestManager {

    private static final RequestManager INSTANCE = new RequestManager();
    
    private final Map<String, RequestPacket> requests = new ConcurrentHashMap<>();
    
    private RequestManager() {}

    public RequestPacket send(RequestPacket packet) {
        packet.prepare();
        Network.getInstance().sendPacket(packet);
        requests.put(packet.getRequestId(), packet);
        return packet;
    }

    public void remove(String requestId) {
        requests.remove(requestId);
    }
    
    public void remove(RequestPacket packet) {
        if (packet.getRequestId() != null) {
            requests.remove(packet.getRequestId());
        }
    }

    public void resolve(ResponsePacket responsePacket) {
        RequestPacket requestPacket = requests.get(responsePacket.getRequestId());
        if (requestPacket != null) {
            requestPacket.invokeCallbacks(false, responsePacket, null);
        }
    }

    public void reject(RequestPacket packet) {
        if (requests.containsKey(packet.getRequestId())) {
            packet.invokeCallbacks(true, null, new Exception("Request timeout"));
        }
    }

    public int size() {
        return requests.size();
    }

    public void clear() {
        requests.clear();
    }

    public RequestPacket get(String requestId) {
        return requests.get(requestId);
    }

    public Map<String, RequestPacket> getAll() {
        return new ConcurrentHashMap<>(requests);
    }

    public static RequestManager getInstance() {
        return INSTANCE;
    }
}