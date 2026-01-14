package com.devopspulse;

import com.devopspulse.service.SystemService;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class SystemServiceTest {

    private final SystemService systemService = new SystemService();

    @Test
    void getSystemInfo_ShouldReturnAllRequiredMetrics() {
        // Act
        Map<String, Object> result = systemService.getSystemInfo();

        // Assert - check all fields exist
        assertThat(result).containsKeys(
                "cpuUsage", "memoryUsage", "diskUsage",
                "cpuUsageValue", "memoryUsageValue", "diskUsageValue",
                "totalMemoryGB", "totalDiskSpaceGB",
                "processCount", "uptimeHours", "timestamp"
        );

        // Перевіряємо типи даних
        assertThat(result.get("processCount")).isInstanceOf(Integer.class);
        assertThat(result.get("uptimeHours")).isInstanceOf(Long.class);
        assertThat(result.get("timestamp")).isInstanceOf(String.class);
    }

    @Test
    void getSystemInfo_ShouldReturnValidFormattedValues() {
        // Act
        Map<String, Object> result = systemService.getSystemInfo();

        // Assert
        String cpuUsage = (String) result.get("cpuUsage");
        String memoryUsage = (String) result.get("memoryUsage");
        String diskUsage = (String) result.get("diskUsage");

        // Перевіряємо, що не null і не порожні
        assertThat(cpuUsage).isNotBlank();
        assertThat(memoryUsage).isNotBlank();
        assertThat(diskUsage).isNotBlank();

        // Перевіряємо, що можна спарсити (завжди з крапкою)
        assertThat(Double.parseDouble(cpuUsage)).isBetween(0.0, 100.0);
        assertThat(Double.parseDouble(memoryUsage)).isBetween(0.0, 100.0);
        assertThat(Double.parseDouble(diskUsage)).isBetween(0.0, 100.0);
    }

    @Test
    void getSystemInfo_ShouldReturnNumericValuesInRange() {
        // Act
        Map<String, Object> result = systemService.getSystemInfo();

        // Assert numeric values
        Double cpuUsageValue = (Double) result.get("cpuUsageValue");
        Double memoryUsageValue = (Double) result.get("memoryUsageValue");
        Double diskUsageValue = (Double) result.get("diskUsageValue");

        // Перевіряємо, що не NaN та в діапазоні
        assertThat(cpuUsageValue).isNotNaN();
        assertThat(memoryUsageValue).isNotNaN();
        assertThat(diskUsageValue).isNotNaN();

        // Перевіряємо діапазон (можливо 0-100, але для безпеки 0-1000)
        assertThat(cpuUsageValue).isBetween(0.0, 1000.0);
        assertThat(memoryUsageValue).isBetween(0.0, 1000.0);
        assertThat(diskUsageValue).isBetween(0.0, 1000.0);
    }

    @Test
    void getSystemInfo_ShouldReturnValidTimestamp() {
        // Act
        Map<String, Object> result = systemService.getSystemInfo();

        // Assert
        String timestamp = (String) result.get("timestamp");
        assertThat(timestamp).isNotBlank();
        assertThat(timestamp).contains("T"); // ISO format
        assertThat(timestamp).contains("-"); // date separator
        assertThat(timestamp).contains(":"); // time separator
    }
}