package ru.university.student.model;

import java.util.Objects;

public class ProcessInfo {
    private final int pid;
    private final String name;
    private final double cpuUsage;
    private final long memoryUsageMb;

    public ProcessInfo(int pid, String name, double cpuUsage, long memoryUsageMb) {
        this.pid = pid;
        this.name = name;
        this.cpuUsage = cpuUsage;
        this.memoryUsageMb = memoryUsageMb;
    }

    public int getPid() {
        return pid;
    }

    public String getName() {
        return name;
    }

    public double getCpuUsage() {
        return cpuUsage;
    }

    public long getMemoryUsageMb() {
        return memoryUsageMb;
    }

    @Override
    public String toString() {
        return String.format("PID: %d, CPU: %.1f%%, MEM: %dMB, Name: %s",
                pid, cpuUsage, memoryUsageMb, name);
    }

}