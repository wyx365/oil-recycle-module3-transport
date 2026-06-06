package com.example.oilrecycle.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
/**
 * 模块3：无人机转运与接口监控。
 */
public class TransportTaskDTO {
    @NotNull
    private Long stationId;
    @NotBlank
    private String startPoint;
    @NotBlank
    private String endPoint;
    @NotBlank
    private String oilType;
    @NotNull
    private BigDecimal weight;
    private String priority;
    private String remark;
}
