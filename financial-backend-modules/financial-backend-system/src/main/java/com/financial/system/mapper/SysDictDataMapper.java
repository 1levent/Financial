package com.financial.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.financial.system.api.domain.SysDictData;

/**
 * 字典表 数据层
 * 
 * @author xinyi
 */
@Mapper
public interface SysDictDataMapper extends BaseMapper<SysDictData> {

}
