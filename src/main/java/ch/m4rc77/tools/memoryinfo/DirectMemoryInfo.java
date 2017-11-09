package ch.m4rc77.tools.memoryinfo;

import lombok.Data;

@Data
class DirectMemoryInfo {

    private double directUsedMemoryMB = -1;

    private double directMemoryCount = -1;

    private double directFreeMemoryNowMB = -1;

    private double directMaxMemoryMB = -1;

    private double directMemoryUsagePercent = -1;

    private String info = "";

}
