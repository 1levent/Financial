package com.financial.system.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 用户和角色关联 sys_user_role
 * 
 * @author xinyi
 */
@TableName("sys_user_role")
@Data
@Schema(name="用户角色关联表")
public class SysUserRole {
    /** 用户ID */
    private Long userId;
    
    /** 角色ID */
    private Long roleId;
}
