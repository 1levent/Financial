package com.financial.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import java.util.List;

import org.springframework.stereotype.Service;
import com.financial.system.api.domain.SysLogininfor;
import com.financial.system.mapper.SysLogininforMapper;
import com.financial.system.service.ISysLogininforService;

/**
 * 系统访问日志情况信息 服务层处理
 * 
 * @author xinyi
 */
@Service
public class SysLogininforServiceImpl extends ServiceImpl<SysLogininforMapper, SysLogininfor> implements ISysLogininforService {

    @Resource
    private SysLogininforMapper logininforMapper;

}
