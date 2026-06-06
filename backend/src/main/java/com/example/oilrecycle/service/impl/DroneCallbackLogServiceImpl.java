package com.example.oilrecycle.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.oilrecycle.entity.DroneCallbackLog;
import com.example.oilrecycle.mapper.DroneCallbackLogMapper;
import com.example.oilrecycle.service.DroneCallbackLogService;
import org.springframework.stereotype.Service;

@Service
/**
 * 模块3：无人机转运与接口监控。
 * DroneCallbackLogServiceImpl 业务实现类，负责核心业务规则和状态流转。
 */
public class DroneCallbackLogServiceImpl extends ServiceImpl<DroneCallbackLogMapper, DroneCallbackLog> implements DroneCallbackLogService {
}
