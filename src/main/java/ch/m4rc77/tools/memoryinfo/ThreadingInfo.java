package ch.m4rc77.tools.memoryinfo;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
class ThreadingInfo {

    private long peakThreadCount = -1;

    private long threadCount = -1;

    Map<Thread.State, Long> threadStates = new HashMap<>();

    private String info = "";

}
