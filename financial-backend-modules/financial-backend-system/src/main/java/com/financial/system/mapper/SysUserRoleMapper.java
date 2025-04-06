package com.financial.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.financial.system.domain.SysUserRole;

/**
 * 用户与角色关联表 数据层
 * 
 * @author xinyi
 */
@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {
    /**
     * 通过用户ID删除用户和角色关联
     * 
     * @param userId 用户ID
     * @return 结果
     */
    public int deleteUserRoleByUserId(Long userId);


    /**
     * 通过角色ID查询角色使用数量
     * 
     * @param roleId 角色ID
     * @return 结果
     */
    public int countUserRoleByRoleId(Long roleId);

    /**
     * 批量取消授权用户角色
     * 
     * @param roleId 角色ID
     * @param userIds 需要删除的用户数据ID
     * @return 结果
     */
    @Delete("delete from sys_user_role where role_id=#{roleId} and user_id in " +
            "<foreach collection='userIds' item='userId' open='(' separator=',' close=')'>" +
            "#{userId}" +
            "</foreach>")
    public int deleteUserRoleInfos(@Param("roleId") Long roleId, @Param("userIds") Long[] userIds);

    /**
     * 通过用户ID和角色ID删除用户和角色关联
     * @param userId 用户id
     * @param roleId 角色id
     * @return 删除结果
     */
    @Delete("delete from sys_user_role where user_id=#{userId} and role_id=#{roleId}")
    int deleteUserRoleInfo(@Param("userId") Long userId, @Param("roleId") Long roleId);

    /**
     * 添加用户和角色关联
     * @param roleId 角色id
     * @param userId 用户id
     * @return 新增结果
     */
    @Insert("insert into sys_user_role(role_id,user_id) values(#{roleId},#{userId})")
    int insertUserRoleByUserIdAndRoleId(@Param("roleId")Long roleId,@Param("userId") Long userId);
}
