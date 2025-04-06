package com.financial.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import java.util.List;

import org.springframework.stereotype.Service;
import com.financial.system.api.domain.SysOperLog;
import com.financial.system.mapper.SysOperLogMapper;
import com.financial.system.service.ISysOperLogService;

/**
 * 操作日志 服务层处理
 * 
 * @author xinyi
 */
@Service
public class SysOperLogServiceImpl extends ServiceImpl<SysOperLogMapper, SysOperLog> implements ISysOperLogService {
    @Resource
    private SysOperLogMapper operLogMapper;

}
