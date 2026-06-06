package com.example.oilrecycle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.oilrecycle.entity.InterfaceStatus;
import org.apache.ibatis.annotations.Mapper;

@Mapper
/**
 * 模块3：无人机转运与接口监控。
 * InterfaceStatusMapper Mapper，负责数据库访问。
 */
public interface InterfaceStatusMapper extends BaseMapper<InterfaceStatus> {
}
