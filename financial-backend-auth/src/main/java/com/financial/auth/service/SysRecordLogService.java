package com.financial.auth.service;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import com.financial.common.core.constant.Constants;
import com.financial.common.core.constant.SecurityConstants;
import com.financial.common.core.utils.StringUtils;
import com.financial.common.core.utils.ip.IpUtils;
import com.financial.system.api.RemoteLogService;
import com.financial.system.api.domain.SysLogininfor;

/**
 * 记录日志方法
 * 
 * @author xinyi
 */
@Component
public class SysRecordLogService {
    @Resource
    private RemoteLogService remoteLogService;

    /**
     * 记录登录信息
     * 
     * @param username 用户名
     * @param status 状态
     * @param message 消息内容
     * @return
     */
    public void recordLogininfor(String username, String status, String message) {
        SysLogininfor logininfor = new SysLogininfor();
        logininfor.setUserName(username);
        logininfor.setIpaddr(IpUtils.getIpAddr());
        logininfor.setMsg(message);
        // 日志状态
        if (StringUtils.equalsAny(status, Constants.LOGIN_SUCCESS, Constants.LOGOUT, Constants.REGISTER)) {
            logininfor.setStatus(Constants.LOGIN_SUCCESS_STATUS);
        } else if (Constants.LOGIN_FAIL.equals(status)) {
            logininfor.setStatus(Constants.LOGIN_FAIL_STATUS);
        }
        remoteLogService.saveLogininfor(logininfor, SecurityConstants.INNER);
    }
}
