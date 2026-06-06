package com.example.oilrecycle.service;

import com.example.oilrecycle.dto.DroneCallbackDTO;
import com.example.oilrecycle.dto.TransportTaskDTO;
import com.example.oilrecycle.entity.TransportTask;

/**
 * 模块3：无人机转运与接口监控。
 * TransportBizService 服务接口，定义业务能力边界。
 */
public interface TransportBizService {
    TransportTask create(TransportTaskDTO dto);

    TransportTask push(Long id);

    void callback(DroneCallbackDTO dto);
}
