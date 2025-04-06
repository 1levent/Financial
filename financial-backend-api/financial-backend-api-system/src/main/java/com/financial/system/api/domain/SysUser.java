package com.financial.system.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serial;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.financial.common.core.annotation.Excel;
import com.financial.common.core.annotation.Excel.ColumnType;
import com.financial.common.core.annotation.Excel.Type;
import com.financial.common.core.web.domain.BaseEntity;
import org.apache.ibatis.type.JdbcType;

/**
 * 用户对象 sys_user
 * 
 * @author xinyi
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_user")
public class SysUser extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    @TableId(type = IdType.AUTO)
    @Excel(name = "用户序号", type = Type.EXPORT, cellType = ColumnType.NUMERIC, prompt = "用户编号")
    private Long userId;

    /** 用户账号 */
    @TableField(value = "user_name",jdbcType = JdbcType.VARCHAR)
    @Excel(name = "登录名称")
    private String userName;

    /** 用户昵称 */
    @TableField(value = "nick_name",jdbcType = JdbcType.VARCHAR)
    @Excel(name = "用户名称")
    private String nickName;

    /** 用户邮箱 */
    @TableField(value = "email",jdbcType = JdbcType.VARCHAR)
    @Excel(name = "用户邮箱")
    private String email;

    /** 手机号码 */
    @TableField(value = "phonenumber",jdbcType = JdbcType.VARCHAR)
    @Excel(name = "手机号码", cellType = ColumnType.TEXT)
    private String phonenumber;

    /** 用户性别 */
    @TableField(value = "sex",jdbcType = JdbcType.CHAR)
    @Excel(name = "用户性别", readConverterExp = "0=男,1=女,2=未知")
    private String sex;

    /** 用户头像 */
    @TableField(value = "avatar",jdbcType = JdbcType.VARCHAR)
    private String avatar;

    /** 密码 */
    @TableField(value = "password",jdbcType = JdbcType.VARCHAR)
    private String password;

    /** 帐号状态（0正常 1停用） */
    @TableField(value = "status",jdbcType = JdbcType.CHAR)
    @Excel(name = "帐号状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 1代表删除） */
    @TableField(value = "deleted",jdbcType = JdbcType.TINYINT)
    private boolean deleted;

    /** 最后登录IP */
    @TableField(value = "login_ip",jdbcType = JdbcType.VARCHAR)
    @Excel(name = "最后登录IP", type = Type.EXPORT)
    private String loginIp;

    /** 最后登录时间 */
    @TableField(value = "login_date",jdbcType = JdbcType.TIMESTAMP)
    @Excel(name = "最后登录时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Type.EXPORT)
    private Date loginDate;

    @TableField(exist = false)
    /** 角色对象 */
    private List<SysRole> roles;

    @TableField(exist = false)
    /** 角色组 */
    private Long[] roleIds;

    @TableField(exist = false)
    /** 角色ID */
    private Long roleId;

    // todo 判断是不是管理员不应该在这里吧？
    public boolean isAdmin() {
        return isAdmin(this.userId);
    }

    public static boolean isAdmin(Long userId) {
        return userId != null && 1L == userId;
    }
}
