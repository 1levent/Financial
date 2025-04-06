package com.financial.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import com.financial.system.api.domain.SysOperLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 操作日志 数据层
 * 
 * @author xinyi
 */
@Mapper
public interface SysOperLogMapper extends BaseMapper<SysOperLog> {

}
