package ch.m4rc77.tools.memoryinfo;

import lombok.Data;

@Data
class HeapMemoryInfo {

    private double heapUsedMemoryMB;

    private double heapFreeMemoryNowMB;

    private double heapFreeMemoryTotalMB;

    private double heapMaxMemoryNowMB;

    private double heapMaxMemoryTotalMB;

    private double heapMemoryUsagePercent;

    private String info;

}
