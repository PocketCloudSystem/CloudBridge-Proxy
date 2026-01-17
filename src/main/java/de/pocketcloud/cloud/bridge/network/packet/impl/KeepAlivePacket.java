package de.pocketcloud.cloud.bridge.network.packet.impl;

import de.pocketcloud.cloud.bridge.CloudBridge;
import de.pocketcloud.cloud.bridge.network.packet.ClientboundPacket;
import de.pocketcloud.cloud.bridge.network.packet.CloudboundPacket;
import de.pocketcloud.cloud.bridge.network.packet.CloudPacket;
import de.pocketcloud.cloud.bridge.network.packet.util.PacketData;
import de.pocketcloud.cloud.bridge.util.ProcessUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public final class KeepAlivePacket extends CloudPacket implements ClientboundPacket, CloudboundPacket {

    private double tps;
    private double avgTps;
    private double memoryUsage;
    private double memoryPeak;
    private double memoryLimit;
    private double cpuUsage;

    public KeepAlivePacket(double tps, double avgTps, double memoryUsage, double memoryPeak, double memoryLimit, double cpuUsage) {
        this.tps = tps;
        this.avgTps = avgTps;
        this.memoryUsage = memoryUsage;
        this.memoryPeak = memoryPeak;
        this.memoryLimit = memoryLimit;
        this.cpuUsage = cpuUsage;
    }

    @Override
    public void handle() {
        CloudBridge.getInstance().setLastKeepAliveCheck((int) (System.currentTimeMillis() / 1000));
        create().sendPacket();
    }
    
    @Override
    public void encodePayload(PacketData packetData) {
        packetData.writeAll(tps, avgTps, memoryUsage, memoryPeak, memoryLimit, cpuUsage);
    }
    
    @Override
    public void decodePayload(PacketData packetData) {
        tps = packetData.readDouble();
        avgTps = packetData.readDouble();
        memoryUsage = packetData.readDouble();
        memoryPeak = packetData.readDouble();
        memoryLimit = packetData.readDouble();
        cpuUsage = packetData.readDouble();
    }
    
    public static KeepAlivePacket create() {
        ProcessUtils.ProcessStatus status = ProcessUtils.getProcessStatus();
        return new KeepAlivePacket(-1, -1, status.rss, status.rssPeak, ProcessUtils.getMemoryLimit(), ProcessUtils.getCpuUsage());
    }
}