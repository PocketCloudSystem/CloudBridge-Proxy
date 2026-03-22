package de.pocketcloud.cloud.bridge.traffic;

import de.pocketcloud.cloud.bridge.traffic.impl.NetworkTrafficMonitor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public final class TrafficMonitorManager {

    public static final String TRAFFIC_NETWORK = "network";

    @Getter
    private static TrafficMonitorManager instance;

    /** type -> factory */
    @Getter
    private final Map<String, Supplier<? extends TrafficMonitor>> trafficMonitorTypes = new HashMap<>();
    /** type -> list of active monitors */
    @Getter
    private final Map<String, List<TrafficMonitor>> trafficMonitors = new HashMap<>();
    /** type -> (mode -> all-time bytes) */
    @Getter
    private final Map<String, Map<String, Long>> allTimeTraffic = new HashMap<>();
    /** type -> (mode -> recent byte history as [timestampSeconds, bytes]) */
    private final Map<String, Map<String, List<double[]>>> byteHistory = new HashMap<>();

    public TrafficMonitorManager() {
        instance = this;
        registerTrafficMonitorType(TRAFFIC_NETWORK, NetworkTrafficMonitor::new);
    }

    public void registerTrafficMonitorType(String type, Supplier<? extends TrafficMonitor> factory) {
        registerTrafficMonitorType(type, factory, false);
    }

    public void registerTrafficMonitorType(String type, Supplier<? extends TrafficMonitor> factory, boolean override) {
        if (trafficMonitorTypes.containsKey(type) && !override) return;
        trafficMonitorTypes.put(type, factory);

        Map<String, Long> allTime = new HashMap<>();
        allTime.put(TrafficMonitor.REGULAR_MODE_IN, 0L);
        allTime.put(TrafficMonitor.REGULAR_MODE_OUT, 0L);
        allTime.put(TrafficMonitor.REGULAR_MODE_IN + TrafficMonitor.SUFFIX_AVG, 0L);
        allTime.put(TrafficMonitor.REGULAR_MODE_OUT + TrafficMonitor.SUFFIX_AVG, 0L);
        allTimeTraffic.put(type, allTime);

        Map<String, List<double[]>> history = new HashMap<>();
        history.put(TrafficMonitor.REGULAR_MODE_IN, new ArrayList<>());
        history.put(TrafficMonitor.REGULAR_MODE_OUT, new ArrayList<>());
        byteHistory.put(type, history);
    }

    public void tick(int currentTick) {
        if (currentTick % 20 == 0) {
            cleanupHistory();
            trafficMonitors.values().forEach(monitors ->
                monitors.forEach(TrafficMonitor::cleanupHistory)
            );
        }
    }

    private void cleanupHistory() {
        double threshold = TrafficMonitor.currentTime() - 1.0;

        for (Map.Entry<String, Map<String, List<double[]>>> typeEntry : byteHistory.entrySet()) {
            String type = typeEntry.getKey();
            for (Map.Entry<String, List<double[]>> modeEntry : typeEntry.getValue().entrySet()) {
                String mode = modeEntry.getKey();
                List<double[]> history = modeEntry.getValue();
                history.removeIf(data -> data[0] < threshold);

                long avg = (long) history.stream().mapToDouble(data -> data[1]).sum();
                allTimeTraffic.get(type).put(mode + TrafficMonitor.SUFFIX_AVG, avg);
            }
        }
    }

    public NetworkTrafficMonitor createNetworkMonitor() {
        TrafficMonitor monitor = createTrafficMonitor(TRAFFIC_NETWORK);
        if (!(monitor instanceof NetworkTrafficMonitor networkMonitor)) {
            throw new IllegalStateException("Registered monitor factory for traffic type " + TRAFFIC_NETWORK + " did not produce a NetworkTrafficMonitor");
        }
        return networkMonitor;
    }

    public TrafficMonitor createTrafficMonitor(String type) {
        if (!trafficMonitorTypes.containsKey(type)) return null;
        TrafficMonitor monitor = trafficMonitorTypes.get(type).get();
        trafficMonitors.computeIfAbsent(type, k -> new ArrayList<>()).add(monitor);
        return monitor;
    }

    public void removeTrafficMonitor(TrafficMonitor monitor) {
        List<TrafficMonitor> monitors = trafficMonitors.get(monitor.getMonitorType());
        if (monitors != null) monitors.remove(monitor);
    }

    public void pushBytes(String type, long bytes, String mode) {
        Map<String, Long> typeTraffic = allTimeTraffic.get(type);
        if (typeTraffic == null || !typeTraffic.containsKey(mode)) return;

        typeTraffic.merge(mode, bytes, Long::sum);

        Map<String, List<double[]>> typeHistory = byteHistory.get(type);
        if (typeHistory != null) {
            typeHistory.computeIfAbsent(mode, k -> new ArrayList<>())
                       .add(new double[]{TrafficMonitor.currentTime(), bytes});
        }

        List<TrafficMonitor> monitors = trafficMonitors.get(type);
        if (monitors != null) {
            for (TrafficMonitor monitor : monitors) {
                monitor.pushBytes(mode, bytes);
            }
        }
    }

    public void callHandlers(String type, String mode, Object... args) {
        List<TrafficMonitor> monitors = trafficMonitors.get(type);
        if (monitors != null) {
            for (TrafficMonitor monitor : monitors) {
                monitor.callHandlers(mode, args);
            }
        }
    }

    public List<TrafficMonitor> getTrafficMonitors(String type) {
        return trafficMonitors.get(type);
    }

    public Map<String, Long> getAllTimeTraffic(String type) {
        return allTimeTraffic.get(type);
    }
}