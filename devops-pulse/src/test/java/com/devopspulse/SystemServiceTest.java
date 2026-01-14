package com.devopspulse;

import com.devopspulse.service.SystemService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class SystemServiceTest {

    private final SystemService systemService = new SystemService();

    @Test
    void getSystemInfo_ShouldReturnAllRequiredMetrics() {
        // Act
        Map<String, Object> result = systemService.getSystemInfo();

        // Assert
        assertThat(result).containsKeys(
                "cpuUsage", "memoryUsage", "diskUsage",
                "cpuUsageValue", "memoryUsageValue", "diskUsageValue",
                "totalMemoryGB", "totalDiskSpaceGB",
                "processCount", "uptimeHours", "timestamp"
        );

        Double cpuUsage = (Double) result.get("cpuUsageValue");
        assertThat(cpuUsage).isBetween(0.0, 100.0);

        Double memoryUsage = (Double) result.get("memoryUsageValue");
        assertThat(memoryUsage).isBetween(0.0, 100.0);

        Double diskUsage = (Double) result.get("diskUsageValue");
        assertThat(diskUsage).isBetween(0.0, 100.0);

        Integer processCount = (Integer) result.get("processCount");
        assertThat(processCount).isPositive();

        Long uptimeHours = (Long) result.get("uptimeHours");
        assertThat(uptimeHours).isGreaterThanOrEqualTo(0L);
    }

    @Test
    void getSystemInfo_ShouldFormatValuesCorrectly() {
        // Act
        Map<String, Object> result = systemService.getSystemInfo();

        // Assert
        String cpuUsage = (String) result.get("cpuUsage");
        String memoryUsage = (String) result.get("memoryUsage");
        String diskUsage = (String) result.get("diskUsage");

        assertThat(cpuUsage).matches("\\d+(\\.\\d{1,2})?");
        assertThat(memoryUsage).matches("\\d+(\\.\\d{1,2})?");
        assertThat(diskUsage).matches("\\d+(\\.\\d{1,2})?");
    }

    @Test
    void getSystemInfo_ShouldReturnValidTimestamp() {
        // Act
        Map<String, Object> result = systemService.getSystemInfo();

        // Assert
        String timestamp = (String) result.get("timestamp");
        assertThat(timestamp).isNotBlank();
        assertThat(timestamp).contains("T"); // ISO format
        assertThat(timestamp).contains(":");
    }

    @Test
    void getSystemInfo_ShouldReturnNumericAndFormattedValues() {
        // Act
        Map<String, Object> result = systemService.getSystemInfo();

        // Assert
        Double cpuUsageValue = (Double) result.get("cpuUsageValue");
        String cpuUsageFormatted = (String) result.get("cpuUsage");

        assertThat(cpuUsageValue).isNotNull();
        assertThat(cpuUsageFormatted).isNotNull();

        double formattedAsDouble = Double.parseDouble(cpuUsageFormatted);
        assertThat(formattedAsDouble).isBetween(0.0, 100.0);
    }
}