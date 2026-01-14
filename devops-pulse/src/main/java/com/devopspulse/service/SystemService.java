package com.devopspulse.service;

import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
public class SystemService {

    private final DecimalFormat decimalFormat;

    public SystemService() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setDecimalSeparator('.');
        decimalFormat = new DecimalFormat("#.##", symbols);
    }

    public Map<String, Object> getSystemInfo() {
        Map<String, Object> systemInfo = new HashMap<>();

        try {
            SystemInfo si = new SystemInfo();
            HardwareAbstractionLayer hal = si.getHardware();


            CentralProcessor cpu = hal.getProcessor();
            double cpuLoad = cpu.getSystemCpuLoad(1000) * 100;
            cpuLoad = validateDouble(cpuLoad);


            GlobalMemory memory = hal.getMemory();
            double memoryUsage = (memory.getTotal() > 0)
                    ? (memory.getTotal() - memory.getAvailable()) * 100.0 / memory.getTotal()
                    : 0.0;
            double totalMemoryGB = (memory.getTotal() > 0)
                    ? memory.getTotal() / (1024.0 * 1024.0 * 1024.0)
                    : 0.0;
            memoryUsage = validateDouble(memoryUsage);


            double diskUsage = 0.0;
            double totalSpaceGB = 0.0;
            try {
                File[] roots = File.listRoots();
                if (roots != null && roots.length > 0) {
                    File disk = roots[0];
                    long totalSpace = disk.getTotalSpace();
                    long freeSpace = disk.getFreeSpace();
                    if (totalSpace > 0) {
                        diskUsage = 100.0 - (freeSpace * 100.0 / totalSpace);
                        totalSpaceGB = totalSpace / (1024.0 * 1024.0 * 1024.0);
                    }
                }
            } catch (SecurityException e) {

                diskUsage = 0.0;
                totalSpaceGB = 0.0;
            }
            diskUsage = validateDouble(diskUsage);


            OperatingSystem os = si.getOperatingSystem();
            int processCount = os.getProcessCount();
            long uptime = os.getSystemUptime();


            systemInfo.put("cpuUsage", decimalFormat.format(cpuLoad));
            systemInfo.put("memoryUsage", decimalFormat.format(memoryUsage));
            systemInfo.put("diskUsage", decimalFormat.format(diskUsage));


            systemInfo.put("cpuUsageValue", cpuLoad);
            systemInfo.put("memoryUsageValue", memoryUsage);
            systemInfo.put("diskUsageValue", diskUsage);


            systemInfo.put("totalMemoryGB", decimalFormat.format(totalMemoryGB));
            systemInfo.put("totalDiskSpaceGB", decimalFormat.format(totalSpaceGB));
            systemInfo.put("processCount", processCount);
            systemInfo.put("uptimeHours", uptime / 3600);
            systemInfo.put("timestamp", LocalDateTime.now().toString());

        } catch (Exception e) {
            systemInfo.put("cpuUsage", "0.00");
            systemInfo.put("memoryUsage", "0.00");
            systemInfo.put("diskUsage", "0.00");
            systemInfo.put("cpuUsageValue", 0.0);
            systemInfo.put("memoryUsageValue", 0.0);
            systemInfo.put("diskUsageValue", 0.0);
            systemInfo.put("totalMemoryGB", "0.00");
            systemInfo.put("totalDiskSpaceGB", "0.00");
            systemInfo.put("processCount", 0);
            systemInfo.put("uptimeHours", 0L);
            systemInfo.put("timestamp", LocalDateTime.now().toString());
        }

        return systemInfo;
    }


    private double validateDouble(double value) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            return 0.0;
        }
        return value;
    }
}