package com.financial.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import com.financial.system.api.domain.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 角色表 数据层
 * 
 * @author xinyi
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * 根据用户ID查询角色
     * 
     * @param userId 用户ID
     * @return 角色列表
     */
    @Select("SELECT r.* FROM sys_role r " +
            "LEFT JOIN sys_user_role ur ON r.role_id = ur.role_id " +
            "WHERE ur.user_id = #{userId}")
    public List<SysRole> selectRolePermissionByUserId(Long userId);



    /**
     * 根据用户ID获取角色选择框列表
     * 
     * @param userId 用户ID
     * @return 选中角色ID列表
     */
    @Select("SELECT r.role_id FROM sys_role r " +
            "LEFT JOIN sys_user_role ur ON r.role_id = ur.role_id " +
            "WHERE ur.user_id = #{userId}")
    public List<Long> selectRoleListByUserId(Long userId);


    /**
     * 根据用户ID查询角色
     * 
     * @param userName 用户名
     * @return 角色列表
     */
    @Select("SELECT r.* FROM sys_role r " +
            "LEFT JOIN sys_user_role ur ON r.role_id = ur.role_id " +
            "LEFT JOIN sys_user u ON ur.user_id = u.user_id " +
            "WHERE u.user_name = #{userName}")
    public List<SysRole> selectRolesByUserName(String userName);

    /**
     * 校验角色名称是否唯一
     * 
     * @param roleName 角色名称
     * @return 角色信息
     */
    @Select("SELECT * FROM sys_role WHERE role_name = #{roleName}")
    public SysRole checkRoleNameUnique(String roleName);

    /**
     * 校验角色权限是否唯一
     * 
     * @param roleKey 角色权限
     * @return 角色信息
     */
    @Select("SELECT * FROM sys_role WHERE role_key = #{roleKey}")
    public SysRole checkRoleKeyUnique(String roleKey);

    /**
     * 根据角色ID查询信息
     * @param roleId 角色ID
     * @return 结果
     */
    @Select("SELECT * FROM sys_role WHERE role_id = #{roleId}")
    SysRole selectByRoleId(Long roleId);
}
