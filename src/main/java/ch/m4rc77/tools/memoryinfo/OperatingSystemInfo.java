package ch.m4rc77.tools.memoryinfo;

import lombok.Data;

@Data
class OperatingSystemInfo {

    private int availableProcessorCores = -1;

    private double TotalPhysicalMemoryMB = -1;

    private long openFileDescriptorCount = -1;

    private double committedVirtualMemoryMB = -1;

    private double freePhysicalMemoryMB = -1;

    private String info = "";

}
