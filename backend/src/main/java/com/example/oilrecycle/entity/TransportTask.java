package com.example.oilrecycle.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("transport_task")
/**
 * 模块3：无人机转运与接口监控。
 * TransportTask 实体类，对应数据库表结构。
 */
public class TransportTask {
    @TableId(type = IdType.AUTO)
    // 主键ID。
    private Long id;
    // 转运任务编号。
    private String taskNo;
    // 所属回收站点ID。
    private Long stationId;
    // 转运起点。
    private String startPoint;
    // 转运终点。
    private String endPoint;
    // 废油类型，HOUSEHOLD表示家用油，RESTAURANT表示餐饮废油。
    private String oilType;
    // 重量，单位千克。
    private BigDecimal weight;
    // 任务优先级。
    private String priority;
    // 业务状态。
    private String status;
    // 无人机编号。
    private String droneNo;
    // 推送无人机系统时间。
    private LocalDateTime pushedTime;
    // 任务完成时间。
    private LocalDateTime finishedTime;
    // 失败原因。
    private String failReason;
    // 备注信息。
    private String remark;
    @TableField(fill = FieldFill.INSERT)
    // 创建时间。
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    // 更新时间。
    private LocalDateTime updateTime;
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    // 逻辑删除标记，0表示未删除，1表示已删除。
    private Integer deleted;
}
