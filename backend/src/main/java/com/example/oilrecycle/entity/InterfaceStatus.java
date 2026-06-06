package com.example.oilrecycle.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("interface_status")
/**
 * 模块3：无人机转运与接口监控。
 * InterfaceStatus 实体类，对应数据库表结构。
 */
public class InterfaceStatus {
    @TableId(type = IdType.AUTO)
    // 主键ID。
    private Long id;
    // 接口名称。
    private String interfaceName;
    // 最近推送时间。
    private LocalDateTime lastPushTime;
    // 成功次数。
    private Integer successCount;
    // 失败次数。
    private Integer failCount;
    // 最近错误信息。
    private String lastError;
    // 业务状态。
    private String status;
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
