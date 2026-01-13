package com.devopspulse.controller;

import io.micrometer.observation.annotation.ObservationKeyValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")


public class SystemController {

    @GetMapping("/system")
    public Map<String, Object> getSystemInfo(){
        Map<String, Object> systemInfo = new HashMap<>();
        systemInfo.put("cpuUsage", 15.5);
        systemInfo.put("memoryUsage", 62.5);
        systemInfo.put("diskUsage", 42.4);
        systemInfo.put("status", "healthy");
        systemInfo.put("timestamp", 1744651234);
        return systemInfo;
    }


    @GetMapping("/health")
    public Map<String, Object> customHealthCheck(){
        Map<String, Object> healthInfo = new HashMap<>();
        healthInfo.put("status", "UP");
        healthInfo.put("service", "DevOps Pulse API");
        healthInfo.put("version", "1.0.0");
        return healthInfo;
    }
}
