package com.example.oilrecycle.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.oilrecycle.common.result.Result;
import com.example.oilrecycle.dto.DroneCallbackDTO;
import com.example.oilrecycle.dto.TransportTaskDTO;
import com.example.oilrecycle.entity.DroneCallbackLog;
import com.example.oilrecycle.entity.TransportTask;
import com.example.oilrecycle.mapper.DroneCallbackLogMapper;
import com.example.oilrecycle.mapper.TransportTaskMapper;
import com.example.oilrecycle.service.TransportBizService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
/**
 * 模块3：无人机转运与接口监控。
 * TransportController 控制器，负责接收前端请求并调用业务服务。
 */
public class TransportController {
    // 转运任务数据访问对象。
    private final TransportTaskMapper taskMapper;
    // logMapper 属性，用于承载业务数据。
    private final DroneCallbackLogMapper logMapper;
    // transportBizService 属性，用于承载业务数据。
    private final TransportBizService transportBizService;

    public TransportController(TransportTaskMapper taskMapper, DroneCallbackLogMapper logMapper, TransportBizService transportBizService) {
        this.taskMapper = taskMapper;
        this.logMapper = logMapper;
        this.transportBizService = transportBizService;
    }

    @GetMapping("/api/transport-tasks")
    public Result<Page<TransportTask>> page(@RequestParam(defaultValue = "1") long page, @RequestParam(defaultValue = "10") long size) {
        return Result.ok(taskMapper.selectPage(new Page<>(page, size), null));
    }

    @PostMapping("/api/transport-tasks")
    public Result<TransportTask> create(@RequestBody @Valid TransportTaskDTO dto) {
        return Result.ok(transportBizService.create(dto));
    }

    @PostMapping("/api/transport-tasks/{id}/push")
    public Result<TransportTask> push(@PathVariable Long id) {
        return Result.ok(transportBizService.push(id));
    }

    @PostMapping("/api/transport-tasks/push")
    public Result<TransportTask> pushCompat(@RequestParam Long id) {
        return Result.ok(transportBizService.push(id));
    }

    @PostMapping("/api/drone/callback")
    public Result<Void> callback(@RequestBody DroneCallbackDTO dto) {
        transportBizService.callback(dto);
        return Result.ok();
    }

    @GetMapping("/api/drone/callback/logs")
    public Result<Page<DroneCallbackLog>> logs(@RequestParam(defaultValue = "1") long page, @RequestParam(defaultValue = "10") long size) {
        return Result.ok(logMapper.selectPage(new Page<>(page, size), null));
    }
}
