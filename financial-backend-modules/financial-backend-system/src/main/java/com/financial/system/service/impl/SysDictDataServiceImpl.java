package com.financial.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;

import org.springframework.stereotype.Service;
import com.financial.system.api.domain.SysDictData;
import com.financial.system.mapper.SysDictDataMapper;
import com.financial.system.service.ISysDictDataService;

/**
 * 字典 业务层处理
 * 
 * @author xinyi
 */
@Service
public class SysDictDataServiceImpl extends ServiceImpl<SysDictDataMapper, SysDictData> implements ISysDictDataService {
    @Resource
    private SysDictDataMapper dictDataMapper;

}
