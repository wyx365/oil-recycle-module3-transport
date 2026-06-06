package com.example.oilrecycle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.oilrecycle.entity.TransportTask;
import org.apache.ibatis.annotations.Mapper;

@Mapper
/**
 * 模块3：无人机转运与接口监控。
 * TransportTaskMapper Mapper，负责数据库访问。
 */
public interface TransportTaskMapper extends BaseMapper<TransportTask> {
}
