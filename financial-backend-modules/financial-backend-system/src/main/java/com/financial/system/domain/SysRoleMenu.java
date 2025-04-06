package com.financial.system.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 角色和菜单关联 sys_role_menu
 * 
 * @author xinyi
 */
@TableName("sys_role_menu")
@Data
@Schema(name="角色菜单关联表")
public class SysRoleMenu {
    /** 角色ID */
    private Long roleId;
    
    /** 菜单ID */
    private Long menuId;
}
