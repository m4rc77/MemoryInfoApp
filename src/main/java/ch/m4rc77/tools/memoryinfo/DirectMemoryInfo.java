package ch.m4rc77.tools.memoryinfo;

import lombok.Data;

@Data
class DirectMemoryInfo {

    private double directUsedMemoryMB;

    private double directMemoryCount;

    private double directFreeMemoryNowMB;

    private double directMaxMemoryMB;

    private double directMemoryUsagePercent;

    private String info;

}
