package com.financial.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import com.financial.system.api.domain.SysDictType;
import org.apache.ibatis.annotations.Mapper;

/**
 * 字典表 数据层
 * 
 * @author xinyi
 */
@Mapper
public interface SysDictTypeMapper extends BaseMapper<SysDictType> {

}
