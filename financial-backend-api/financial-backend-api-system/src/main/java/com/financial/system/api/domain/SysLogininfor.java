package com.financial.system.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 系统访问记录表 sys_logininfor
 * 
 * @author xinyi
 */
@Data
@TableName("sys_logininfor")
public class SysLogininfor implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /* ID */
    private Long infoId;

    /** 用户账号 */
    private String userName;

    /** 状态 0成功 1失败 */
    private String status;

    /** 地址 */
    private String ipaddr;

    /** 描述 */
    private String msg;

    /** 访问时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date accessTime;
}