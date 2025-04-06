package com.financial.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import com.financial.system.domain.SysRoleMenu;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 角色与菜单关联表 数据层
 * 
 * @author xinyi
 */
@Mapper
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {
    /**
     * 查询菜单使用数量
     * 
     * @param menuId 菜单ID
     * @return 结果
     */
    @Select("select count(1) from sys_role_menu where menu_id = #{menuId}")
    public int checkMenuExistRole(@Param("menuId")Long menuId);

    /**
     * 通过角色ID删除角色和菜单关联
     * 
     * @param roleId 角色ID
     * @return 结果
     */
    @Delete("delete from sys_role_menu where role_id = #{roleId}")
    public int deleteRoleMenuByRoleId(@Param("roleId") Long roleId);

    /**
     * 通过角色ID集合删除角色和菜单关联
     * @param roleIds 角色ID集合
     */
    @Delete("delete from sys_role_menu where role_id in " +
            "<foreach collection='roleIds' item='roleId' open='(' separator=',' close=')'>" +
            "#{roleId}" +
            "</foreach>")
    void deleteRoleMenuByRoleIds(@Param("roleIds")Long[] roleIds);
}
