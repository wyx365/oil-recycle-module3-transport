package com.example.oilrecycle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.oilrecycle.entity.DroneCallbackLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
/**
 * 模块3：无人机转运与接口监控。
 * DroneCallbackLogMapper Mapper，负责数据库访问。
 */
public interface DroneCallbackLogMapper extends BaseMapper<DroneCallbackLog> {
}
