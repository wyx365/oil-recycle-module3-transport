package com.example.oilrecycle.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
/**
 * 模块3：无人机转运与接口监控。
 */
public class DroneCallbackDTO {
    private String taskNo;
    private String droneNo;
    private String status;
    private String currentLocation;
    private BigDecimal battery;
    private String message;
    private LocalDateTime callbackTime;
}
