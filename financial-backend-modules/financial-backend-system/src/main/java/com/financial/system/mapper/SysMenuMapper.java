package com.financial.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.financial.system.domain.SysMenu;

/**
 * 菜单表 数据层
 * 
 * @author xinyi
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    /**
     * 根据用户所有权限
     * 
     * @return 权限列表
     */
    @Select("SELECT DISTINCT perms FROM sys_menu WHERE perms IS NOT NULL AND perms <> ''")
    public List<String> selectMenuPerms();

    /**
     * 根据用户查询系统菜单列表
     * 
     * @param menu 菜单信息
     * @return 菜单列表
     */
    @Select("SELECT * FROM sys_menu WHERE user_id = #{menu.userId}")
    public List<SysMenu> selectMenuListByUserId(SysMenu menu);

    /**
     * 根据角色ID查询权限
     * 
     * @param roleId 角色ID
     * @return 权限列表
     */
    @Select("SELECT DISTINCT perms FROM sys_menu WHERE role_id = #{roleId}")
    public List<String> selectMenuPermsByRoleId(Long roleId);

    /**
     * 根据用户ID查询权限
     * 
     * @param userId 用户ID
     * @return 权限列表
     */
    @Select("SELECT DISTINCT m.perms FROM sys_menu m " +
            "LEFT JOIN sys_role_menu rm ON m.menu_id = rm.menu_id " +
            "LEFT JOIN sys_user_role ur ON rm.role_id = ur.role_id " +
            "WHERE ur.user_id = #{userId}")
    public List<String> selectMenuPermsByUserId(Long userId);

    /**
     * 根据用户ID查询菜单
     * 
     * @return 菜单列表
     */
    @Select("SELECT * FROM sys_menu")
    public List<SysMenu> selectMenuTreeAll();

    /**
     * 根据用户ID查询菜单
     * 
     * @param userId 用户ID
     * @return 菜单列表
     */
    @Select("SELECT * FROM sys_menu WHERE user_id = #{userId}")
    public List<SysMenu> selectMenuTreeByUserId(Long userId);

    /**
     * 根据角色ID查询菜单树信息
     * 
     * @param roleId 角色ID
     * @param menuCheckStrictly 菜单树选择项是否关联显示
     * @return 选中菜单列表
     */
    @Select("SELECT menu_id FROM sys_menu WHERE role_id = #{roleId} AND menu_check_strictly = #{menuCheckStrictly}")
    public List<Long> selectMenuListByRoleId(@Param("roleId") Long roleId, @Param("menuCheckStrictly") boolean menuCheckStrictly);

    /**
     * 校验菜单名称是否唯一
     * 
     * @param menuName 菜单名称
     * @param parentId 父菜单ID
     * @return 结果
     */
    @Select("SELECT * FROM sys_menu WHERE menu_name = #{menuName} AND parent_id = #{parentId}")
    public SysMenu checkMenuNameUnique(@Param("menuName") String menuName, @Param("parentId") Long parentId);
}