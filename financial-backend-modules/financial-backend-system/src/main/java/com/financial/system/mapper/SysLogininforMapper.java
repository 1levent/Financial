package com.financial.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import com.financial.system.api.domain.SysLogininfor;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统访问日志情况信息 数据层
 * 
 * @author xinyi
 */
@Mapper
public interface SysLogininforMapper extends BaseMapper<SysLogininfor> {

}
