package com.financial.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import java.util.Set;
import com.financial.system.api.domain.SysRole;
import com.financial.system.domain.SysUserRole;
import org.springframework.transaction.annotation.Transactional;

/**
 * 角色业务层
 * 
 * @author xinyi
 */
public interface ISysRoleService extends IService<SysRole> {


    /**
     * 根据用户ID查询角色列表
     * 
     * @param userId 用户ID
     * @return 角色列表
     */
    public List<SysRole> selectRolesByUserId(Long userId);

    /**
     * 根据用户ID查询角色权限
     * 
     * @param userId 用户ID
     * @return 权限列表
     */
    public Set<String> selectRolePermissionByUserId(Long userId);

    /**
     * 根据用户ID获取角色选择框列表
     * 
     * @param userId 用户ID
     * @return 选中角色ID列表
     */
    public List<Long> selectRoleListByUserId(Long userId);

    /**
     * 校验角色名称是否唯一
     * 
     * @param role 角色信息
     * @return 结果
     */
    public boolean checkRoleNameUnique(SysRole role);

    /**
     * 校验角色权限是否唯一
     * 
     * @param role 角色信息
     * @return 结果
     */
    public boolean checkRoleKeyUnique(SysRole role);

    /**
     * 校验角色是否允许操作
     * 
     * @param role 角色信息
     */
    public void checkRoleAllowed(SysRole role);

    /**
     * 校验角色是否有数据权限
     * 
     * @param roleIds 角色id
     */
    public void checkRoleDataScope(Long... roleIds);

    /**
     * 通过角色ID查询角色使用数量
     * 
     * @param roleId 角色ID
     * @return 结果
     */
    public int countUserRoleByRoleId(Long roleId);

    @Transactional(rollbackFor = Exception.class)
    int insertRole(SysRole role);

    @Transactional(rollbackFor = Exception.class)
    int updateRole(SysRole role);

    /**
     * 修改角色状态
     * 
     * @param role 角色信息
     * @return 结果
     */
    public int updateRoleStatus(SysRole role);

    /**
     * 修改数据权限信息
     * 
     * @param role 角色信息
     * @return 结果
     */
    public int authDataScope(SysRole role);

    @Transactional(rollbackFor = Exception.class)
    int deleteRoleById(Long roleId);

    @Transactional(rollbackFor = Exception.class)
    int deleteRoleByIds(Long[] roleIds);

    /**
     * 取消授权用户角色
     * 
     * @param userRole 用户和角色关联信息
     * @return 结果
     */
    public int deleteAuthUser(SysUserRole userRole);

    /**
     * 批量取消授权用户角色
     * 
     * @param roleId 角色ID
     * @param userIds 需要取消授权的用户数据ID
     * @return 结果
     */
    public int deleteAuthUsers(Long roleId, Long[] userIds);

    /**
     * 批量选择授权用户角色
     * 
     * @param roleId 角色ID
     * @param userIds 需要删除的用户数据ID
     * @return 结果
     */
    public int insertAuthUsers(Long roleId, Long[] userIds);
}
