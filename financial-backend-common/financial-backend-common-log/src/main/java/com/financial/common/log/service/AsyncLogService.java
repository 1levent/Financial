package com.financial.common.log.service;


import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.financial.common.core.constant.SecurityConstants;
import com.financial.system.api.RemoteLogService;
import com.financial.system.api.domain.SysOperLog;

/**
 * 异步调用日志服务
 * 
 * @author xinyi
 */
@Service
public class AsyncLogService {
    @Resource
    private RemoteLogService remoteLogService;

    /**
     * 保存系统日志记录
     */
    @Async
    public void saveSysLog(SysOperLog sysOperLog) throws Exception {
        remoteLogService.saveLog(sysOperLog, SecurityConstants.INNER);
    }
}
