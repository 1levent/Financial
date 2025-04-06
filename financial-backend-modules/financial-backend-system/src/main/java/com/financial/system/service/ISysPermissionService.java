package com.financial.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.Set;

import com.financial.system.api.domain.SysUser;

/**
 * 权限信息 服务层
 * 
 * @author xinyi
 */
public interface ISysPermissionService {
    /**
     * 获取角色数据权限
     * 
     * @param user 用户Id
     * @return 角色权限信息
     */
    public Set<String> getRolePermission(SysUser user);

    /**
     * 获取菜单数据权限
     * 
     * @param user 用户Id
     * @return 菜单权限信息
     */
    public Set<String> getMenuPermission(SysUser user);
}
