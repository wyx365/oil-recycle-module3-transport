package com.example.oilrecycle.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("drone_callback_log")
/**
 * 模块3：无人机转运与接口监控。
 * DroneCallbackLog 实体类，对应数据库表结构。
 */
public class DroneCallbackLog {
    @TableId(type = IdType.AUTO)
    // 主键ID。
    private Long id;
    // 转运任务编号。
    private String taskNo;
    // 无人机编号。
    private String droneNo;
    // 业务状态。
    private String status;
    // 无人机当前位置。
    private String currentLocation;
    // 无人机电量百分比。
    private BigDecimal battery;
    // 回调消息。
    private String message;
    // 回调时间。
    private LocalDateTime callbackTime;
    @TableField(fill = FieldFill.INSERT)
    // 创建时间。
    private LocalDateTime createTime;
}
