package ch.m4rc77.tools.memoryinfo;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.util.HashMap;
import java.util.Map;

public class MemoryInfoService {

    private static final double MB = 1024 * 1024.0;
    private static final String MB_STR = " MB";

    private static final byte OK = 0;
    private static final byte NOK = -1;

    MemoryInfo getMemoryInfo() {
        MemoryInfo info = new MemoryInfo();
        info.setStatus(OK);

        info.setOperatingSystemInfo(createOperatingSystemInfo());

        info.setHeapMemoryInfo(createHeapMemoryInfo());

        info.setDirectMemoryInfo(createDirectMemoryInfo());

        info.setThreadInfo(createThreadInfo());

        fillStatus(info);

        return info;
    }

    private HeapMemoryInfo createHeapMemoryInfo() {
        HeapMemoryInfo info = new HeapMemoryInfo();
        Runtime runtime = Runtime.getRuntime();

        // the current total memory ... may change
        long totalMemory = runtime.totalMemory();
        // the value defined by -Xmx
        long maxMemory = runtime.maxMemory();
        // the currently free memory
        long freeMemory = runtime.freeMemory();
        long usedMemory = (totalMemory - freeMemory);
        long presumableFreeMemory = maxMemory - usedMemory;

        double memUsage = (((double) usedMemory) / maxMemory) * 100;

        info.setHeapUsedMemoryMB(toMB(usedMemory));
        info.setHeapFreeMemoryNowMB(toMB(freeMemory));
        info.setHeapFreeMemoryTotalMB(toMB(presumableFreeMemory));
        info.setHeapMaxMemoryNowMB(toMB(totalMemory));
        info.setHeapMaxMemoryTotalMB(toMB(maxMemory));
        info.setHeapMemoryUsagePercent(round(memUsage));

        StringBuilder output = new StringBuilder();
        output.append("Java Heap Memory Size:").append("\n");
        output.append("    Used Memory:       ").append(round(usedMemory / MB)).append(MB_STR).append("\n");
        output.append("    Free Memory:       ").append(round(freeMemory / MB)).append(MB_STR).append(" (now)").append("\n");
        output.append("    Free Memory:       ").append(round(presumableFreeMemory / MB)).append(MB_STR).append(" (total)").append("\n");
        output.append("    Max Memory:        ").append(round(totalMemory / MB)).append(MB_STR).append(" (now)").append("\n");
        output.append("    Max Memory:        ").append(round(maxMemory / MB)).append(MB_STR + " (total)").append("\n");
        output.append("    Java Memory usage: ").append(round(memUsage)).append("% (total) ").append("\n");
        info.setInfo(output.toString());

        return info;
    }

    private DirectMemoryInfo createDirectMemoryInfo() {
        DirectMemoryInfo info = new DirectMemoryInfo();

        // msc: using sun.misc.* classes to get information about direct memory is not so nice but currently the only
        // usable way to get a precise information about direct memory usage. It would also be possible to get some
        // information about direct memory over JMX but not the information maxDirectMemory.
        // See the following URLs for more information:
        // http://stackoverflow.com/questions/20058489/is-there-a-way-to-measure-direct-memory-usage-in-java
        // https://www.javacodegeeks.com/2016/02/default-hotspot-maximum-direct-memory-size.html
        // https://blogs.oracle.com/alanb/entry/monitoring_direct_buffers
        // https://hub.jmonkeyengine.org/t/monitor-direct-memory-usage-in-your-app/25422
        long maxDirectMemoryValue = sun.misc.VM.maxDirectMemory();
        long reservedDirectMemoryValue = sun.misc.SharedSecrets.getJavaNioAccess().getDirectBufferPool().getMemoryUsed();
        long countDirectMemoryValue = sun.misc.SharedSecrets.getJavaNioAccess().getDirectBufferPool().getCount();
        long freeDirectMemory = maxDirectMemoryValue - reservedDirectMemoryValue;
        // what does us tell this information? ... I don't know
        //long totalCapacityDirectMemoryValue = sun.misc.SharedSecrets.getJavaNioAccess().getDirectBufferPool().getTotalCapacity();

        double directMemUsage = (((double) reservedDirectMemoryValue) / maxDirectMemoryValue) * 100;

        info.setDirectUsedMemoryMB(toMB(reservedDirectMemoryValue));
        info.setDirectMemoryCount(countDirectMemoryValue);
        info.setDirectFreeMemoryNowMB(toMB(freeDirectMemory));
        info.setDirectMaxMemoryMB(toMB(maxDirectMemoryValue));
        info.setDirectMemoryUsagePercent(round(directMemUsage));

        StringBuilder output = new StringBuilder();
        output.append("DirectMemory Size").append("\n");
        output.append("    Used DirectMemory:      ").append(round(reservedDirectMemoryValue / MB)).append(MB_STR + " (now)").append("\n");
        output.append("    DirectMemory Count:     ").append(countDirectMemoryValue).append(" (now)").append("\n");
        output.append("    Free DirectMemory:      ").append(round(freeDirectMemory / MB)).append(MB_STR + " (now)").append("\n");
        output.append("    Max DirectMemory:       ").append(round(maxDirectMemoryValue / MB)).append(MB_STR).append(" (now)").append("\n");
        output.append("    DirectMemory usage:     ").append(round(directMemUsage)).append("% (total) ").append("\n");
        info.setInfo(output.toString());

        return info;
    }

    private OperatingSystemInfo createOperatingSystemInfo() {

        OperatingSystemInfo info = new OperatingSystemInfo();

        int cores = Runtime.getRuntime().availableProcessors();
        info.setAvailableProcessorCores(cores);

        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        Object attribute;

        try {
            ObjectName os = new ObjectName("java.lang", "type", "OperatingSystem");

            attribute = mBeanServer.getAttribute(os, "TotalPhysicalMemorySize");
            double totalPhysicalMemoryMB = toMB(Long.parseLong(attribute.toString()));
            info.setTotalPhysicalMemoryMB(totalPhysicalMemoryMB);

            attribute = mBeanServer.getAttribute(os, "OpenFileDescriptorCount");
            long openFileDescriptorCount = Long.parseLong(attribute.toString());
            info.setOpenFileDescriptorCount(openFileDescriptorCount);

            attribute = mBeanServer.getAttribute(os, "CommittedVirtualMemorySize");
            double committedVirtualMemoryMB = toMB(Long.parseLong(attribute.toString()));
            info.setCommittedVirtualMemoryMB(committedVirtualMemoryMB);

            attribute = mBeanServer.getAttribute(os, "FreePhysicalMemorySize");
            double freePhysicalMemoryMB = toMB(Long.parseLong(attribute.toString()));
            info.setFreePhysicalMemoryMB(freePhysicalMemoryMB);

            StringBuilder output = new StringBuilder();
            output.append("Operating System Info").append("\n");
            output.append("    Available processors cores: ").append(cores).append("\n");
            output.append("    Open file descriptors:      ").append(openFileDescriptorCount).append("\n");
            output.append("    Total physical memory:      ").append(totalPhysicalMemoryMB).append(MB_STR).append("\n");
            output.append("    Committed virtual memory:   ").append(committedVirtualMemoryMB).append(MB_STR).append("\n");
            output.append("    Free physical memory:       ").append(freePhysicalMemoryMB).append(MB_STR).append("\n");
            info.setInfo(output.toString());

        } catch (Exception e) {
            info.setInfo("Error: " + e);
        }

        return info;
    }

    private ThreadingInfo createThreadInfo() {
        boolean ok = true;

        ThreadingInfo info = new ThreadingInfo();

        long peakThreadCount = ManagementFactory.getThreadMXBean().getPeakThreadCount();
        info.setPeakThreadCount(peakThreadCount);

        long threadCount = ManagementFactory.getThreadMXBean().getThreadCount();
        info.setThreadCount(threadCount);

        ThreadInfo[] infos = ManagementFactory.getThreadMXBean().dumpAllThreads(false, false);

        Map<Thread.State, Long> tstat = new HashMap<>();
        for (Thread.State state : Thread.State.values()) {
            tstat.put(state, 0L);
        }
        for (ThreadInfo tinfo : infos) {
            Thread.State state = tinfo.getThreadState();
            tstat.put(state, tstat.get(state) + 1);
        }
        info.setThreadStates(tstat);

        StringBuilder output = new StringBuilder();
        output.append("Thread Info").append("\n");
        output.append("    Peak thread count:    ").append(peakThreadCount).append("\n");
        output.append("    Current thread count: ").append(threadCount).append("\n");
        output.append("    Thread states:    \n");
        for (Thread.State state : Thread.State.values()) {
            output.append("                  ").append(String.format("%1$-15s", state + ": ")).append(tstat.get(state)).append("\n");
        }
        info.setInfo(output.toString());

        return info;
    }

    private void fillStatus(MemoryInfo info) {

        double totalMemory = info.getHeapMemoryInfo().getHeapMaxMemoryTotalMB();
        double maxMemory = info.getHeapMemoryInfo().getHeapMaxMemoryNowMB();

        double memUsage = info.getHeapMemoryInfo().getHeapMemoryUsagePercent();


        if (Math.abs(maxMemory - totalMemory) < 1 && memUsage > 80) {
            info.setStatus(NOK);
            info.setStatusInfo("HEAP Memory is high!");
        }

        double directMemUsage = info.getDirectMemoryInfo().getDirectMemoryUsagePercent();
        if (directMemUsage > 80) {
            info.setStatus(NOK);
            info.setStatusInfo("Direct Memory is high!");
        }

        if (info.getThreadInfo().getThreadStates().get(Thread.State.BLOCKED) > 0) {
            info.setStatus(NOK);
            info.setStatusInfo("Blocked Threads detected!");
        }

    }

    private static double round(double v) {
        return (Math.round(v * 10) / 10.0);
    }

    private double toMB(long bytes) {
        return round(bytes / MB);
    }

}
