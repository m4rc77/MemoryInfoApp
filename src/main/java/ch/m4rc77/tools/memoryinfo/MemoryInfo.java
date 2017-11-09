package ch.m4rc77.tools.memoryinfo;

import lombok.Data;

@Data
class MemoryInfo {

    private String info;

    private String status;

    private String statusInfo;

    private double totalMemoryMB;

    private int availableProcessorCores;

    private long openFileDescriptorCount;

    private HeapMemoryInfo heapMemoryInfo;

    private DirectMemoryInfo directMemoryInfo;

}
