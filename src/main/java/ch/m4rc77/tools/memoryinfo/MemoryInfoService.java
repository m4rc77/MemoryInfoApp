package ch.m4rc77.tools.memoryinfo;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import java.lang.management.ManagementFactory;

public class MemoryInfoService {

    private static final double MB = 1024 * 1024.0;
    private static final String MB_STR = " MB";

    private static final String OK = "OK";
    private static final String NOK = "NOK";
    private static final String ERROR = "ERROR";
    private static final String WARNING = "WARNING";

    MemoryInfo getMemoryInfo() {
        MemoryInfo info = new MemoryInfo();
        info.setStatus(OK);
        info.setStatusInfo("");

        try {
            MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
//         05-Jul-12 10:08:00	java.lang:type=OperatingSystem.MaxFileDescriptorCount:	10000
//         05-Jul-12 10:08:00	java.lang:type=OperatingSystem.OpenFileDescriptorCount:	164
//         05-Jul-12 10:08:00	java.lang:type=OperatingSystem.CommittedVirtualMemorySize:	1811673088
//         05-Jul-12 10:08:00	java.lang:type=OperatingSystem.FreePhysicalMemorySize:	133595136
//         05-Jul-12 10:08:00	java.lang:type=OperatingSystem.FreeSwapSpaceSize:	6497226752
//         05-Jul-12 10:08:00	java.lang:type=OperatingSystem.ProcessCpuTime:	7170530000000
//         05-Jul-12 10:08:00	java.lang:type=OperatingSystem.TotalPhysicalMemorySize:	3426328576
//         05-Jul-12 10:08:00	java.lang:type=OperatingSystem.TotalSwapSpaceSize:	6537863168
//         05-Jul-12 10:08:00	java.lang:type=OperatingSystem.Name:	Linux
            Object attribute = mBeanServer.getAttribute(new ObjectName("java.lang", "type", "OperatingSystem"), "TotalPhysicalMemorySize");
            info.setTotalMemoryMB(toMB(Long.parseLong(attribute.toString())));

            Object attributeCount = mBeanServer.getAttribute(new ObjectName("java.lang", "type", "OperatingSystem"), "OpenFileDescriptorCount");
            info.setOpenFileDescriptorCount(Long.parseLong(attributeCount.toString()));
        } catch (Exception e) {
            info.setStatus(NOK);
        }

        info.setHeapMemoryInfo(createHeapMemoryInfo());

        info.setDirectMemoryInfo(createDirectMemoryInfo());

        fillOperatingSystemInformation(info);

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
        output.append("    Used Memory:      ").append(round(usedMemory / MB)).append(MB_STR).append("\n");
        // Print free memory
        output.append("    Free Memory:      ").append(round(freeMemory / MB)).append(MB_STR).append(" (now)").append("\n");
        output.append("    Free Memory:      ").append(round(presumableFreeMemory / MB)).append(MB_STR).append(" (total)").append("\n");
        // Print total available memory
        output.append("    Max Memory:       ").append(round(totalMemory / MB)).append(MB_STR).append(" (now)").append("\n");
        // Print Maximum available memory
        output.append("    Max Memory:       ").append(round(maxMemory / MB)).append(MB_STR + " (total)").append("\n");
        output.append("\n");
        output.append("    Java Memory usage:     ").append(round(memUsage)).append("% (total) ").append("\n");
        output.append("\n\n");

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

        // Print direct memory information ...
        StringBuilder output = new StringBuilder();
        output.append("DirectMemory Size").append("\n");
        output.append("    Used DirectMemory:      ").append(round(reservedDirectMemoryValue / MB)).append(MB_STR + " (now)").append("\n");
        output.append("    DirectMemory Count:     ").append(countDirectMemoryValue).append(" (now)").append("\n");
        output.append("    Free DirectMemory:      ").append(round(freeDirectMemory / MB)).append(MB_STR + " (now)").append("\n");
        output.append("    Max DirectMemory:       ").append(round(maxDirectMemoryValue / MB)).append(MB_STR).append(" (now)").append("\n");
        output.append("\n");
        output.append("    DirectMemory usage:     ").append(round(directMemUsage)).append("% (total) ").append("\n");
        output.append("\n\n");
        info.setInfo(output.toString());

        return info;
    }

    private void fillOperatingSystemInformation(MemoryInfo info) {

        int cores = Runtime.getRuntime().availableProcessors();
        info.setAvailableProcessorCores(cores);

        StringBuilder output = new StringBuilder();
        output.append("\n");
        output.append("Available processors (cores): ").append(cores).append("\n");
        output.append("\n\n");

        info.setInfo(output.toString());
    }

    private void fillStatus(MemoryInfo info) {

        double totalMemory = info.getHeapMemoryInfo().getHeapMaxMemoryTotalMB();
        double maxMemory = info.getHeapMemoryInfo().getHeapMaxMemoryNowMB();

        double memUsage = info.getHeapMemoryInfo().getHeapMemoryUsagePercent();


        if (Math.abs(maxMemory - totalMemory) < 1 && memUsage > 80) {
            info.setStatus(WARNING);
            info.setStatusInfo("HEAP Memory is high!");
        }

//      String directMemWarning = "";
//      if (directMemUsage > 0.8) {
//         directMemWarning = WARNING;
//         // status = NOK;
//      }

    }

    private static double round(double v) {
        return (Math.round(v * 10) / 10.0);
    }

    private double toMB(long bytes) {
        return round(bytes / MB);
    }

//   private static void createServerInfo() {
//      ServerInfo info = new ServerInfo();
//
//      OperatingSystemMXBean operatingSystemMXBean = java.lang.management.ManagementFactory.getOperatingSystemMXBean();
//      ThreadMXBean threadMXBean = java.lang.management.ManagementFactory.getThreadMXBean();
//      RuntimeMXBean runtimeMXBean = java.lang.management.ManagementFactory.getRuntimeMXBean();
//
//      int cores = Runtime.getRuntime().availableProcessors();
//      double loadAvg = operatingSystemMXBean.getSystemLoadAverage();
//
//      int threadCount = threadMXBean.getThreadCount();
//      long uptime = runtimeMXBean.getUptime();
//
//      info.setCpuCores( cores );
//      info.setUptime( uptime );
//      info.setThreadCount( threadCount );
//      info.setLoadAvg( loadAvg );
//   }

}
