package de.pocketcloud.cloud.bridge.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ProcessUtils {

    private static final Map<Long, CpuSnapshot> cycleSnapshots = new HashMap<>();
    private static final Map<Long, Double> latestResults = new HashMap<>();

    private static Integer clockTicks = null;

    private ProcessUtils() {}

    public static void startCpuRetrieveCycle() {
        startCpuRetrieveCycle(null);
    }

    public static void startCpuRetrieveCycle(Long pid) {
        long actualPid = pid != null ? pid : ProcessHandle.current().pid();
        CpuSnapshot snapshot = getCpuSnapshot(actualPid);
        if (snapshot != null) {
            cycleSnapshots.put(actualPid, snapshot);
        }
    }

    public static void stopCpuRetrieveCycle() {
        stopCpuRetrieveCycle(null);
    }

    public static void stopCpuRetrieveCycle(Long pid) {
        long actualPid = pid != null ? pid : ProcessHandle.current().pid();
        CpuSnapshot firstSnapshot = cycleSnapshots.get(actualPid);
        if (firstSnapshot == null) return;

        CpuSnapshot secondSnapshot = getCpuSnapshot(actualPid);
        if (secondSnapshot != null) {
            Double usage = calculateCpuUsageFromSnapshots(firstSnapshot, secondSnapshot);
            latestResults.put(actualPid, usage != null ? usage : 0.0);
        }
    }

    public static void restartCpuRetrieveCycle() {
        restartCpuRetrieveCycle(null);
    }

    public static void restartCpuRetrieveCycle(Long pid) {
        long actualPid = pid != null ? pid : ProcessHandle.current().pid();
        stopCpuRetrieveCycle(actualPid);
        startCpuRetrieveCycle(actualPid);
    }

    public static Double getCpuUsage() {
        return getCpuUsage(null);
    }

    public static Double getCpuUsage(Long pid) {
        long actualPid = pid != null ? pid : ProcessHandle.current().pid();
        return latestResults.getOrDefault(actualPid, 0.0);
    }

    public static CpuSnapshot getCpuSnapshot(Long pid) {
        String pidStr = pid != null ? String.valueOf(pid) : "self";
        Path statFile = Paths.get("/proc", pidStr, "stat");

        if (!Files.exists(statFile)) return null;

        try {
            String stat = Files.readString(statFile);
            int lastParen = stat.lastIndexOf(')');
            if (lastParen == -1) return null;

            String[] parts = stat.substring(lastParen + 2).split(" ");

            long utime = Long.parseLong(parts[11]);
            long stime = Long.parseLong(parts[12]);

            return new CpuSnapshot(
                    utime,
                    stime,
                    utime + stime,
                    System.nanoTime() / 1_000_000_000.0
            );
        } catch (IOException | NumberFormatException | IndexOutOfBoundsException e) {
            return null;
        }
    }

    public static Double calculateCpuUsageFromSnapshots(CpuSnapshot firstSnapshot, CpuSnapshot secondSnapshot) {
        if (firstSnapshot == null || secondSnapshot == null) return null;

        double timeDiff = secondSnapshot.timestamp() - firstSnapshot.timestamp();
        long cpuDiff = secondSnapshot.total() - firstSnapshot.total();

        if (timeDiff <= 0) return 0.0;

        double cpuUsage = (cpuDiff / (double) getClockTicks()) / timeDiff * 100;
        return Math.round(cpuUsage * 100.0) / 100.0;
    }

    public static ProcessStatus getProcessStatus() {
        return ProcessUtils.getProcessStatus(null);
    }

    public static ProcessStatus getProcessStatus(Long pid) {
        String pidStr = pid != null ? String.valueOf(pid) : "self";
        Path statusFile = Paths.get("/proc", pidStr, "status");

        if (!Files.exists(statusFile)) return null;

        try {
            List<String> lines = Files.readAllLines(statusFile);
            ProcessStatus status = new ProcessStatus();
            Pattern pattern = Pattern.compile("^(VmRSS|VmSize|VmHWM|Threads):\\s+(\\d+)");

            for (String line : lines) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    String key = matcher.group(1);
                    long value = Long.parseLong(matcher.group(2));

                    switch (key) {
                        case "VmRSS" -> status.rss = value * 1024;
                        case "VmHWM" -> status.rssPeak = value * 1024;
                        case "VmSize" -> status.size = value * 1024;
                        case "Threads" -> status.threads = (int) value;
                    }
                }
            }
            return status;
        } catch (IOException e) {
            return null;
        }
    }

    private static int getClockTicks() {
        if (clockTicks == null) {
            try {
                Process process = Runtime.getRuntime().exec(new String[]{"getconf", "CLK_TCK"});
                String output = new String(process.getInputStream().readAllBytes()).trim();
                clockTicks = output.matches("\\d+") ? Integer.parseInt(output) : 100;
            } catch (IOException | NumberFormatException e) {
                clockTicks = 100;
            }
        }
        return clockTicks;
    }

    public static long getMemoryLimit() {
        long maxMemory = Runtime.getRuntime().maxMemory();
        return (maxMemory == Long.MAX_VALUE) ? -1 : maxMemory;
    }

    public static void kill(long pid, boolean subprocesses) throws IOException {
        if (!subprocesses) {
            ProcessHandle.of(pid).ifPresent(ProcessHandle::destroyForcibly);
        } else {
            Runtime.getRuntime().exec(new String[]{"kill", "-9", String.valueOf(-pid)});
        }
    }

    public static class ProcessStatus {
        public long rss = 0;
        public long rssPeak = 0;
        public long size = 0;
        public int threads = 0;

        @Override
        public String toString() {
            return String.format("ProcessStatus{rss=%d, rssPeak=%d, size=%d, threads=%d}", rss, rssPeak, size, threads);
        }
    }

    public record CpuSnapshot(long utime, long stime, long total, double timestamp) {}
}