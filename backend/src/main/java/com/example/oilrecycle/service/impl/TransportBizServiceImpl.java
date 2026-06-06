package com.example.oilrecycle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.oilrecycle.common.exception.BizException;
import com.example.oilrecycle.dto.DroneCallbackDTO;
import com.example.oilrecycle.dto.TransportTaskDTO;
import com.example.oilrecycle.entity.DroneCallbackLog;
import com.example.oilrecycle.entity.InterfaceStatus;
import com.example.oilrecycle.entity.TransportTask;
import com.example.oilrecycle.mapper.DroneCallbackLogMapper;
import com.example.oilrecycle.mapper.InterfaceStatusMapper;
import com.example.oilrecycle.mapper.TransportTaskMapper;
import com.example.oilrecycle.service.StorageBizService;
import com.example.oilrecycle.service.TransportBizService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 无人机转运任务业务实现类。
 * 负责我方平台与另一组无人机调度平台之间的模拟推送、状态回传和库存扣减闭环。
 */
@Service
/**
 * 模块3：无人机转运与接口监控。
 */
public class TransportBizServiceImpl implements TransportBizService {

    // 转运任务数据访问对象，负责 transport_task 表操作。
    private final TransportTaskMapper taskMapper;
    // 无人机回调日志数据访问对象，负责保存每一次状态回传。
    private final DroneCallbackLogMapper callbackLogMapper;
    // 接口状态数据访问对象，负责记录两套系统对接健康状态。
    private final InterfaceStatusMapper interfaceStatusMapper;
    // 库存业务服务，转运到达或完成后用于扣减站点库存。
    private final StorageBizService storageBizService;

    /**
     * 构造方法：注入转运任务、回调日志、接口状态和库存服务。
     */
    public TransportBizServiceImpl(TransportTaskMapper taskMapper,
                                   DroneCallbackLogMapper callbackLogMapper,
                                   InterfaceStatusMapper interfaceStatusMapper,
                                   StorageBizService storageBizService) {
        this.taskMapper = taskMapper;
        this.callbackLogMapper = callbackLogMapper;
        this.interfaceStatusMapper = interfaceStatusMapper;
        this.storageBizService = storageBizService;
    }

    /**
     * 创建废油转运任务。
     * 站点库存达到一定量后，由工作人员发起站点到集中处理点的无人机转运需求。
     */
    @Override
    public TransportTask create(TransportTaskDTO dto) {
        // 核心逻辑：生成转运任务编号，作为与无人机系统交互的唯一业务标识。
        String taskNo = "TT" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + (int) (Math.random() * 900 + 100);

        // 核心逻辑：组装转运任务，初始状态为已创建，等待推送到无人机平台。
        TransportTask task = new TransportTask();
        task.setTaskNo(taskNo);
        task.setStationId(dto.getStationId());
        task.setStartPoint(dto.getStartPoint());
        task.setEndPoint(dto.getEndPoint());
        task.setOilType(dto.getOilType());
        task.setWeight(dto.getWeight());
        task.setPriority(dto.getPriority() == null ? "NORMAL" : dto.getPriority());
        task.setStatus("CREATED");
        task.setRemark(dto.getRemark());
        taskMapper.insert(task);
        return task;
    }

    /**
     * 模拟推送转运任务到无人机调度平台。
     * 课程设计中不接真实无人机系统，因此这里使用本地状态更新模拟接口调用成功。
     */
    @Override
    @Transactional
    public TransportTask push(Long id) {
        // 核心逻辑：查询转运任务，任务不存在时直接抛出业务异常。
        TransportTask task = taskMapper.selectById(id);
        if (task == null) {
            throw new BizException("转运任务不存在");
        }

        // 核心逻辑：模拟HTTP推送成功后，将任务状态改为已推送并记录推送时间。
        task.setStatus("PUSHED");
        task.setPushedTime(LocalDateTime.now());
        taskMapper.updateById(task);

        // 核心逻辑：更新接口健康状态，便于超级管理员查看对接是否正常。
        InterfaceStatus status = statusRow();
        status.setLastPushTime(LocalDateTime.now());
        status.setSuccessCount((status.getSuccessCount() == null ? 0 : status.getSuccessCount()) + 1);
        status.setStatus("OK");
        status.setLastError(null);
        interfaceStatusMapper.updateById(status);
        return task;
    }

    /**
     * 接收无人机系统状态回调。
     * 对方平台回传派单、飞行中、已送达、已完成或失败状态后，我方更新任务并形成闭环。
     */
    @Override
    @Transactional
    public void callback(DroneCallbackDTO dto) {
        // 核心逻辑：根据转运任务编号查找任务，保证回调能关联到我方业务单据。
        TransportTask task = taskMapper.selectOne(new LambdaQueryWrapper<TransportTask>()
                .eq(TransportTask::getTaskNo, dto.getTaskNo()));
        if (task == null) {
            throw new BizException("转运任务不存在");
        }

        // 核心逻辑：不论回调成功或失败，都先保存原始回调日志，方便问题追溯。
        DroneCallbackLog log = new DroneCallbackLog();
        log.setTaskNo(dto.getTaskNo());
        log.setDroneNo(dto.getDroneNo());
        log.setStatus(dto.getStatus());
        log.setCurrentLocation(dto.getCurrentLocation());
        log.setBattery(dto.getBattery());
        log.setMessage(dto.getMessage());
        log.setCallbackTime(dto.getCallbackTime() == null ? LocalDateTime.now() : dto.getCallbackTime());
        callbackLogMapper.insert(log);

        // 核心逻辑：同步无人机编号和最新任务状态到转运任务主表。
        task.setDroneNo(dto.getDroneNo());
        task.setStatus(dto.getStatus());

        // 核心逻辑：失败状态保存失败原因，便于管理员排查。
        if ("FAILED".equals(dto.getStatus())) {
            task.setFailReason(dto.getMessage());
        }

        // ==================== 模块2：回收预约与站点库存 ====================
        // 核心逻辑：无人机已送达或已完成时，说明废油已离开站点，扣减站点库存。
        if ("ARRIVED".equals(dto.getStatus()) || "FINISHED".equals(dto.getStatus())) {
            task.setFinishedTime(LocalDateTime.now());
            storageBizService.outbound(task.getStationId(), task.getId(), task.getOilType(), task.getWeight(), 0L, "无人机转运完成出库");
        }

        // ==================== 模块3：无人机转运与接口监控 ====================
        // 核心逻辑：保存任务最新状态，前端转运记录页面可实时展示。
        taskMapper.updateById(task);
    }

    /**
     * 获取或初始化无人机接口状态行。
     * 如果数据库没有 DRONE_PLATFORM 记录，则自动创建，保证监控页面始终有数据。
     */
    private InterfaceStatus statusRow() {
        // 核心逻辑：按照接口名称查询唯一状态记录。
        InterfaceStatus status = interfaceStatusMapper.selectOne(new LambdaQueryWrapper<InterfaceStatus>()
                .eq(InterfaceStatus::getInterfaceName, "DRONE_PLATFORM"));

        // 核心逻辑：首次推送时自动初始化接口状态，减少初始化数据依赖。
        if (status == null) {
            status = new InterfaceStatus();
            status.setInterfaceName("DRONE_PLATFORM");
            status.setSuccessCount(0);
            status.setFailCount(0);
            status.setStatus("OK");
            interfaceStatusMapper.insert(status);
        }
        return status;
    }
}
