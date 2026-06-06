package com.example.oilrecycle.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.oilrecycle.entity.InterfaceStatus;
import com.example.oilrecycle.mapper.InterfaceStatusMapper;
import com.example.oilrecycle.service.InterfaceStatusService;
import org.springframework.stereotype.Service;

@Service
/**
 * 模块3：无人机转运与接口监控。
 * InterfaceStatusServiceImpl 业务实现类，负责核心业务规则和状态流转。
 */
public class InterfaceStatusServiceImpl extends ServiceImpl<InterfaceStatusMapper, InterfaceStatus> implements InterfaceStatusService {
}
