package com.financial.auth.service;

import com.financial.common.core.context.SecurityContextHolder;
import com.financial.common.core.utils.JsonUtil;
import com.financial.common.core.utils.JwtUtils;
import com.financial.common.core.utils.MapUtils;
import com.financial.common.core.utils.id.SelfTraceIdGenerator;
import com.financial.common.security.service.TokenService;
import com.financial.system.api.model.UserSaveReq;
import jakarta.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.financial.common.core.constant.CacheConstants;
import com.financial.common.core.constant.Constants;
import com.financial.common.core.constant.SecurityConstants;
import com.financial.common.core.constant.UserConstants;
import com.financial.common.core.domain.R;
import com.financial.common.core.enums.UserStatus;
import com.financial.common.core.exception.ServiceException;
import com.financial.common.core.text.Convert;
import com.financial.common.core.utils.DateUtils;
import com.financial.common.core.utils.StringUtils;
import com.financial.common.core.utils.ip.IpUtils;
import com.financial.common.redis.service.RedisService;
import com.financial.common.security.utils.SecurityUtils;
import com.financial.system.api.RemoteUserService;
import com.financial.system.api.domain.SysUser;
import com.financial.system.api.model.LoginUser;

/**
 * 登录校验方法
 * 
 * @author xinyi
 */
@Component
public class SysLoginService {

    //打印日志
    private static final Logger log = LoggerFactory.getLogger(SysLoginService.class);

    @Resource
    private RemoteUserService remoteUserService;

    @Resource
    private SysPasswordService passwordService;

    @Resource
    private SysRecordLogService recordLogService;

    @Resource
    private RedisService redisService;

    @Resource
    private TokenService tokenService;

    /**
     * 登录
     */
    public LoginUser login(String username, String password) {
        // 用户名或密码为空 错误
        if (StringUtils.isAnyBlank(username, password)) {
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, "用户/密码必须填写");
            throw new ServiceException("用户/密码必须填写");
        }
        // 密码如果不在指定范围内 错误
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, "用户密码不在指定范围");
            throw new ServiceException("用户密码不在指定范围");
        }
        // 用户名不在指定范围内 错误
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH) {
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, "用户名不在指定范围");
            throw new ServiceException("用户名不在指定范围");
        }
        // IP黑名单校验
        String blackStr = Convert.toStr(redisService.getCacheObject(CacheConstants.SYS_LOGIN_BLACKIPLIST));
        if (IpUtils.isMatchedIp(blackStr, IpUtils.getIpAddr())) {
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, "很遗憾，访问IP已被列入系统黑名单");
            throw new ServiceException("很遗憾，访问IP已被列入系统黑名单");
        }
        // 查询用户信息
        R<LoginUser> userResult = remoteUserService.getUserInfo(username, SecurityConstants.INNER);

        if (R.FAIL == userResult.getCode()) {
            throw new ServiceException(userResult.getMsg());
        }

        LoginUser userInfo = userResult.getData();
        SysUser user = userResult.getData().getSysUser();
        if (user.isDeleted()) {
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, "对不起，您的账号已被删除");
            throw new ServiceException("对不起，您的账号：" + username + " 已被删除");
        }
        if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, "用户已停用，请联系管理员");
            throw new ServiceException("对不起，您的账号：" + username + " 已停用");
        }
        passwordService.validate(user, password);
        recordLogService.recordLogininfor(username, Constants.LOGIN_SUCCESS, "登录成功");
        recordLoginInfo(user.getUserId());
        return userInfo;
    }

    /**
     * 记录登录信息
     *
     * @param userId 用户ID
     */
    public void recordLoginInfo(Long userId) {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        // 更新用户登录IP
        sysUser.setLoginIp(IpUtils.getIpAddr());
        // 更新用户登录时间
        sysUser.setLoginDate(DateUtils.getNowDate());
        remoteUserService.recordUserLogin(sysUser, SecurityConstants.INNER);
    }

    public void logout(String loginName) {
        recordLogService.recordLogininfor(loginName, Constants.LOGOUT, "退出成功");
    }

    /**
     * 注册
     */
    public void register(String username, String password) {
        // 用户名或密码为空 错误
        if (StringUtils.isAnyBlank(username, password)) {
            throw new ServiceException("用户/密码必须填写");
        }
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH) {
            throw new ServiceException("账户长度必须在2到20个字符之间");
        }
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            throw new ServiceException("密码长度必须在5到20个字符之间");
        }

        // 注册用户信息
        SysUser sysUser = new SysUser();
        sysUser.setUserName(username);
        sysUser.setNickName(username);
        sysUser.setPassword(SecurityUtils.encryptPassword(password));
        System.out.println("加密后得密码："+sysUser.getPassword());
        R<?> registerResult = remoteUserService.registerUserInfo(sysUser, SecurityConstants.INNER);

        if (R.FAIL == registerResult.getCode()) {
            throw new ServiceException(registerResult.getMsg());
        }
        recordLogService.recordLogininfor(username, Constants.REGISTER, "注册成功");
    }

    /**
     * 微信登录
     * @param userName 用户账号
     * @return token
     */
    public String loginByWx(String userName) {
        LoginUser loginUser = remoteUserService.getUserInfo(userName, SecurityConstants.INNER).getData();
        Map<String, Object> map = tokenService.createToken(loginUser);
        return map.get("access_token").toString();
    }

    /**
     * 自动注册微信一个用户
     * @param uuid 微信唯一标识
     * @return userId 用户主键
     */
    public String autoRegisterWxUserInfo(String uuid) {
        UserSaveReq req = new UserSaveReq().setLoginType(1).setThirdAccountId(uuid);
        String userName = registerOrGetUserInfo(req);
        SecurityContextHolder.setUserName(userName);
        return userName;
    }

    /**
     * 没有注册时，先注册一个用户；若已经有，则登录
     * @param req 用户入参
     */
    private String registerOrGetUserInfo(UserSaveReq req) {

        LoginUser user = remoteUserService.getByThirdAccountId(req.getThirdAccountId()).getData();
        if (user == null) {
            return registerByWechat(req.getThirdAccountId());
        }
        return user.getUsername();
    }

    /**
     * 通过微信注册
     * @param thirdAccountId 微信唯一标识
     * @return userId
     */
    private String registerByWechat(String thirdAccountId) {

        // 用户不存在，则需要注册
        // 1. 保存用户登录信息
        SysUser sysUser = new SysUser();
        sysUser.setThirdAccountId(thirdAccountId);
        sysUser.setLoginType("1");
        sysUser.setUserName(thirdAccountId);
        sysUser.setNickName(thirdAccountId);
        remoteUserService.add(sysUser);
        LoginUser loginUser = remoteUserService.getByThirdAccountId(thirdAccountId).getData();
        return loginUser.getUsername();
    }
}
