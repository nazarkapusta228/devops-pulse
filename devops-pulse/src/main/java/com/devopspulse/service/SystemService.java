package com.devopspulse.service;

import org.springframework.stereotype.Service;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

import java.io.File;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;




@Service
public class SystemService {


    public SystemService(){

    }


    public Map<String, Object> getSystemInfo(){
        Map<String, Object> systemInfo = new HashMap<>();
        DecimalFormat df = new DecimalFormat("#.##");

        //Getting system metrics
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();

        // CPU
        CentralProcessor cpu = hal.getProcessor();
        double cpuLoad = cpu.getSystemCpuLoad(1000) * 100;

        // Memory
        GlobalMemory memory = hal.getMemory();
        double memoryUsage = (memory.getTotal() - memory.getAvailable()) * 100.0 / memory.getTotal();
        double totalMemoryGB = memory.getTotal() / (1024.0 * 1024.0 * 1024.0); // Конвертація в GB

        // Disk
        File disk = new File("E:");
        long totalSpace = disk.getTotalSpace();
        long freeSpace = disk.getFreeSpace();
        double diskUsage = 100.0 - (freeSpace * 100.0 / totalSpace);
        double totalSpaceGB = totalSpace / (1024.0 * 1024.0 * 1024.0);

        // Processes & Uptime
        OperatingSystem os = si.getOperatingSystem();
        int processCount = os.getProcessCount();
        long uptime = os.getSystemUptime();

        // formatted values
        systemInfo.put("cpuUsage", df.format(cpuLoad));
        systemInfo.put("memoryUsage", df.format(memoryUsage));
        systemInfo.put("diskUsage", df.format(diskUsage));

        // numeric values
        systemInfo.put("cpuUsageValue", cpuLoad);
        systemInfo.put("memoryUsageValue", memoryUsage);
        systemInfo.put("diskUsageValue", diskUsage);

        //Other system metrics
        systemInfo.put("totalMemoryGB", df.format(totalMemoryGB));
        systemInfo.put("totalDiskSpaceGB", df.format(totalSpaceGB));
        systemInfo.put("processCount", processCount);
        systemInfo.put("uptimeHours", uptime / 3600); // to hours
        systemInfo.put("timestamp", LocalDateTime.now().toString());

        return systemInfo;
    }

}
