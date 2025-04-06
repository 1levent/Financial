package com.financial.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import com.financial.system.domain.SysNotice;
import org.apache.ibatis.annotations.Mapper;

/**
 * 通知公告表 数据层
 * 
 * @author xinyi
 */
@Mapper
public interface SysNoticeMapper extends BaseMapper<SysNotice> {

}