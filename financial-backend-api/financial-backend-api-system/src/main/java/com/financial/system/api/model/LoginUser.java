package com.financial.system.api.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;
import com.financial.system.api.domain.SysUser;
import lombok.Data;

/**
 * 用户信息
 *
 * @author xinyi
 */
@Data
public class LoginUser implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户唯一标识
     */
    private String token;

    /**
     * 用户名id
     */
    private Long userid;

    /**
     * 用户名
     */
    private String username;

    /**
     * 登录时间
     */
    private Long loginTime;

    /**
     * 过期时间
     */
    private Long expireTime;

    /**
     * 登录IP地址
     */
    private String ipaddr;

    /**
     * 权限列表
     */
    private Set<String> permissions;

    /**
     * 角色列表
     */
    private Set<String> roles;

    /**
     * 用户信息
     */
    private SysUser sysUser;

}
