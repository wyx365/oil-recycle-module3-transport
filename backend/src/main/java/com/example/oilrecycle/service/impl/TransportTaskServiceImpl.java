package com.example.oilrecycle.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.oilrecycle.entity.TransportTask;
import com.example.oilrecycle.mapper.TransportTaskMapper;
import com.example.oilrecycle.service.TransportTaskService;
import org.springframework.stereotype.Service;

@Service
/**
 * 模块3：无人机转运与接口监控。
 * TransportTaskServiceImpl 业务实现类，负责核心业务规则和状态流转。
 */
public class TransportTaskServiceImpl extends ServiceImpl<TransportTaskMapper, TransportTask> implements TransportTaskService {
}
