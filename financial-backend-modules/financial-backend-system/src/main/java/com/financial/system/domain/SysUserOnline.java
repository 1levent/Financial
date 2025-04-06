package com.financial.system.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 当前在线会话
 * 
 * @author xinyi
 */
@TableName("sys_user_online")
@Data
@Schema(name="当前在线会话")
public class SysUserOnline {
    /** 会话编号 */
    private String tokenId;

    /** 用户名称 */
    private String userName;

    /** 登录IP地址 */
    private String ipaddr;

    /** 登录地址 */
    private String loginLocation;

    /** 浏览器类型 */
    private String browser;

    /** 操作系统 */
    private String os;

    /** 登录时间 */
    private Long loginTime;
}
