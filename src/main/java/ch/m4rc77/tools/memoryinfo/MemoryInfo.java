package ch.m4rc77.tools.memoryinfo;

import lombok.Data;

@Data
class MemoryInfo {

    private byte status = 0;

    private String statusInfo = "OK";

    private OperatingSystemInfo operatingSystemInfo;

    private HeapMemoryInfo heapMemoryInfo;

    private DirectMemoryInfo directMemoryInfo;

}
