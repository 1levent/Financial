package com.financial.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.financial.common.core.constant.UserConstants;
import com.financial.common.core.exception.ServiceException;
import com.financial.common.core.utils.SpringUtils;
import com.financial.common.core.utils.StringUtils;
import com.financial.common.security.utils.SecurityUtils;
import com.financial.system.api.domain.SysRole;
import com.financial.system.api.domain.SysUser;
import com.financial.system.domain.SysRoleMenu;
import com.financial.system.domain.SysUserRole;
import com.financial.system.mapper.SysRoleMapper;
import com.financial.system.mapper.SysRoleMenuMapper;
import com.financial.system.mapper.SysUserRoleMapper;
import com.financial.system.service.ISysRoleService;

/**
 * 角色 业务层处理
 * 
 * @author xinyi
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {
    @Resource
    private SysRoleMapper roleMapper;

    @Resource
    private SysRoleMenuMapper roleMenuMapper;

    @Resource
    private SysUserRoleMapper userRoleMapper;


    /**
     * 根据用户ID查询角色
     * 
     * @param userId 用户ID
     * @return 角色列表
     */
    @Override
    public List<SysRole> selectRolesByUserId(Long userId) {
        List<SysRole> userRoles = roleMapper.selectRolePermissionByUserId(userId);
        List<SysRole> roles = roleMapper.selectList(new QueryWrapper<>());
        for (SysRole role : roles) {
            for (SysRole userRole : userRoles) {
                if (role.getRoleId().longValue() == userRole.getRoleId().longValue()) {
                    role.setFlag(true);
                    break;
                }
            }
        }
        return roles;
    }

    /**
     * 根据用户ID查询权限
     * 
     * @param userId 用户ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectRolePermissionByUserId(Long userId) {
        List<SysRole> perms = roleMapper.selectRolePermissionByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (SysRole perm : perms) {
            if (StringUtils.isNotNull(perm)) {
                permsSet.addAll(Arrays.asList(perm.getRoleKey().trim().split(",")));
            }
        }
        return permsSet;
    }

    /**
     * 根据用户ID获取角色选择框列表
     * 
     * @param userId 用户ID
     * @return 选中角色ID列表
     */
    @Override
    public List<Long> selectRoleListByUserId(Long userId)
    {
        return roleMapper.selectRoleListByUserId(userId);
    }

    /**
     * 校验角色名称是否唯一
     * 
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public boolean checkRoleNameUnique(SysRole role) {
        Long roleId = StringUtils.isNull(role.getRoleId()) ? -1L : role.getRoleId();
        SysRole info = roleMapper.checkRoleNameUnique(role.getRoleName());
        if (StringUtils.isNotNull(info) && info.getRoleId().longValue() != roleId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验角色权限是否唯一
     * 
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public boolean checkRoleKeyUnique(SysRole role) {
        Long roleId = StringUtils.isNull(role.getRoleId()) ? -1L : role.getRoleId();
        SysRole info = roleMapper.checkRoleKeyUnique(role.getRoleKey());
        if (StringUtils.isNotNull(info) && info.getRoleId().longValue() != roleId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验角色是否允许操作
     * 
     * @param role 角色信息
     */
    @Override
    public void checkRoleAllowed(SysRole role) {
        if (StringUtils.isNotNull(role.getRoleId()) && role.isAdmin()) {
            throw new ServiceException("不允许操作超级管理员角色");
        }
    }

    /**
     * 校验角色是否有数据权限
     * 
     * @param roleIds 角色id
     */
    @Override
    public void checkRoleDataScope(Long... roleIds) {
        if (!SysUser.isAdmin(SecurityUtils.getUserId())) {
            for (Long roleId : roleIds) {
                SysRole role = new SysRole();
                role.setRoleId(roleId);
                List<SysRole> roles = SpringUtils.getAopProxy(this).list(new QueryWrapper<>(role));
                if (StringUtils.isEmpty(roles)) {
                    throw new ServiceException("没有权限访问角色数据！");
                }
            }
        }
    }

    /**
     * 通过角色ID查询角色使用数量
     * 
     * @param roleId 角色ID
     * @return 结果
     */
    @Override
    public int countUserRoleByRoleId(Long roleId) {
        return userRoleMapper.countUserRoleByRoleId(roleId);
    }

    /**
     * 新增保存角色信息
     * 
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertRole(SysRole role) {
        // 新增角色信息
        roleMapper.insert(role);
        return insertRoleMenu(role);
    }

    /**
     * 修改保存角色信息
     * 
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateRole(SysRole role)
    {
        // 修改角色信息
        roleMapper.updateById(role);
        // 删除角色与菜单关联
        roleMenuMapper.deleteRoleMenuByRoleId(role.getRoleId());
        return insertRoleMenu(role);
    }

    /**
     * 修改角色状态
     * 
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public int updateRoleStatus(SysRole role)
    {
        return roleMapper.updateById(role);
    }

    /**
     * 修改数据权限信息
     * todo 可能要删掉
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int authDataScope(SysRole role) {
        // 修改角色信息
        return roleMapper.updateById(role);
    }

    /**
     * 新增角色菜单信息
     * 
     * @param role 角色对象
     */
    public int insertRoleMenu(SysRole role) {
        int rows = 1;
        // 新增用户与角色管理
        List<SysRoleMenu> list = new ArrayList<SysRoleMenu>();
        for (Long menuId : role.getMenuIds()) {
            SysRoleMenu rm = new SysRoleMenu();
            rm.setRoleId(role.getRoleId());
            rm.setMenuId(menuId);
            list.add(rm);
        }
        // todo 后续考虑逻辑
//        if (!list.isEmpty()) {
//            rows = this.saveBatch(list);
//        }
        return rows;
    }

    /**
     * 通过角色ID删除角色
     * 
     * @param roleId 角色ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteRoleById(Long roleId)
    {
        // 删除角色与菜单关联
        roleMenuMapper.deleteRoleMenuByRoleId(roleId);
        return roleMapper.deleteById(roleId);
    }

    /**
     * 批量删除角色信息
     * 
     * @param roleIds 需要删除的角色ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteRoleByIds(Long[] roleIds) {
        for (Long roleId : roleIds) {
            checkRoleAllowed(new SysRole(roleId));
            checkRoleDataScope(roleId);
            SysRole role = getById(roleId);
            if (countUserRoleByRoleId(roleId) > 0) {
                throw new ServiceException(String.format("%1$s已分配,不能删除", role.getRoleName()));
            }
        }
        // 删除角色与菜单关联
        roleMenuMapper.deleteRoleMenuByRoleIds(roleIds);
        return roleMapper.deleteBatchIds(Arrays.asList(roleIds));
    }

    /**
     * 取消授权用户角色
     * 
     * @param userRole 用户和角色关联信息
     * @return 结果
     */
    @Override
    public int deleteAuthUser(SysUserRole userRole) {
        return userRoleMapper.deleteUserRoleInfo(userRole.getUserId(), userRole.getRoleId());
    }

    /**
     * 批量取消授权用户角色
     * 
     * @param roleId 角色ID
     * @param userIds 需要取消授权的用户数据ID
     * @return 结果
     */
    @Override
    public int deleteAuthUsers(Long roleId, Long[] userIds) {
        return userRoleMapper.deleteUserRoleInfos(roleId, userIds);
    }

    /**
     * 批量选择授权用户角色
     * 
     * @param roleId 角色ID
     * @param userIds 需要授权的用户数据ID
     * @return 结果
     */
    @Override
    public int insertAuthUsers(Long roleId, Long[] userIds) {
        // 新增用户与角色管理
        for (Long userId : userIds) {
            userRoleMapper.insertUserRoleByUserIdAndRoleId(roleId, userId);
        }
        //todo 这里后面要仔细考虑一下逻辑
        return 1;
    }
}
