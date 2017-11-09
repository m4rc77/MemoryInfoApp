package ch.m4rc77.tools.memoryinfo;

import lombok.Data;

@Data
class HeapMemoryInfo {

    private double heapUsedMemoryMB = -1;

    private double heapFreeMemoryNowMB = -1;

    private double heapFreeMemoryTotalMB = -1;

    private double heapMaxMemoryNowMB = -1;

    private double heapMaxMemoryTotalMB = -1;

    private double heapMemoryUsagePercent = -1;

    private String info = "";

}
