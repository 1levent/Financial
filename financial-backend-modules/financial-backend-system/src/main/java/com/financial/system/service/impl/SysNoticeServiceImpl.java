package com.financial.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import java.util.List;

import org.springframework.stereotype.Service;
import com.financial.system.domain.SysNotice;
import com.financial.system.mapper.SysNoticeMapper;
import com.financial.system.service.ISysNoticeService;

/**
 * 公告 服务层实现
 * 
 * @author xinyi
 */
@Service
public class SysNoticeServiceImpl extends ServiceImpl<SysNoticeMapper, SysNotice> implements ISysNoticeService {
    @Resource
    private SysNoticeMapper noticeMapper;


}
