package de.pocketcloud.cloud.bridge.traffic;

import lombok.Getter;
import org.apache.logging.log4j.util.InternalApi;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public abstract class TrafficMonitor {

    public static final String REGULAR_MODE_IN = "in";
    public static final String REGULAR_MODE_OUT = "out";
    public static final String SUFFIX_AVG = "_avg";

    @Getter
    protected boolean active = true;
    @Getter
    protected final long timestamp;
    protected Long monitoringDuration = null;

    /** mode -> list of handlers */
    @Getter
    protected final Map<String, List<Consumer<Object[]>>> handlers = new HashMap<>();

    @Getter
    protected long totalBytesIn = 0;
    @Getter
    protected long totalBytesOut = 0;
    /** each entry: [timestampSeconds, bytes] */
    protected final Deque<double[]> byteHistoryIn = new ArrayDeque<>();
    protected final Deque<double[]> byteHistoryOut = new ArrayDeque<>();

    protected Consumer<Object[]> stopMonitoringHandler = null;

    @Getter
    private final String monitorType;

    public TrafficMonitor(String monitorType) {
        this.monitorType = monitorType;
        this.timestamp = currentSeconds();
    }

    /**
     * @param handler (String buffer, long bytesIn, Address source)
     */
    public TrafficMonitor monitorIn(Consumer<Object[]> handler) {
        addHandler(REGULAR_MODE_IN, handler);
        return this;
    }

    /**
     * @param handler (String buffer, long bytesOut, Address destination)
     */
    public TrafficMonitor monitorOut(Consumer<Object[]> handler) {
        addHandler(REGULAR_MODE_OUT, handler);
        return this;
    }

    protected void addHandler(String mode, Consumer<Object[]> handler) {
        handlers.computeIfAbsent(mode, k -> new ArrayList<>()).add(handler);
    }

    @InternalApi
    public void pushBytes(String mode, long bytes) {
        if (!active) return;
        double now = currentTime();
        switch (mode.toLowerCase()) {
            case REGULAR_MODE_IN -> {
                totalBytesIn += bytes;
                byteHistoryIn.add(new double[]{now, bytes});
            }
            case REGULAR_MODE_OUT -> {
                totalBytesOut += bytes;
                byteHistoryOut.add(new double[]{now, bytes});
            }
        }
    }

    @InternalApi
    public void cleanupHistory() {
        double threshold = currentTime() - 1.0;
        while (!byteHistoryIn.isEmpty() && byteHistoryIn.peekFirst()[0] < threshold) byteHistoryIn.pollFirst();
        while (!byteHistoryOut.isEmpty() && byteHistoryOut.peekFirst()[0] < threshold) byteHistoryOut.pollFirst();
    }

    public void callHandlers(String mode, Object... args) {
        if (!active) return;
        List<Consumer<Object[]>> modeHandlers = handlers.get(mode);
        if (modeHandlers != null) {
            for (Consumer<Object[]> handler : modeHandlers) {
                handler.accept(args);
            }
        }
    }

    public void registerStopMonitoringHandler(Consumer<Object[]> handler) {
        this.stopMonitoringHandler = handler;
    }

    public final void stopMonitoring(Object... args) {
        if (!active) return;
        active = false;
        handlers.clear();
        monitoringDuration = currentSeconds() - timestamp;
        TrafficMonitorManager.getInstance().removeTrafficMonitor(this);
        if (!onStopMonitoring(args) && stopMonitoringHandler != null) {
            stopMonitoringHandler.accept(args);
        }
    }

    public boolean onStopMonitoring(Object... args) {
        return false;
    }

    public long getMonitoringDuration() {
        if (monitoringDuration == null) return currentSeconds() - timestamp;
        return monitoringDuration;
    }

    public long getTotalBytes() {
        return totalBytesOut + totalBytesIn;
    }

    public long getAverageTotalBytes() {
        return getAverageBytesOut() + getAverageBytesIn();
    }

    public long getAverageBytesOut() {
        return (long) byteHistoryOut.stream().mapToDouble(data -> data[1]).sum();
    }

    public long getAverageBytesIn() {
        return (long) byteHistoryIn.stream().mapToDouble(data -> data[1]).sum();
    }

    protected static double currentTime() {
        return System.currentTimeMillis() / 1000.0;
    }

    protected static long currentSeconds() {
        return System.currentTimeMillis() / 1000L;
    }
}