package com.financial.system.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.financial.common.core.annotation.Excel;
import com.financial.common.core.annotation.Excel.ColumnType;
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
    @Excel(name = "序号", cellType = ColumnType.NUMERIC)
    private Long infoId;

    /** 用户账号 */
    @Excel(name = "用户账号")
    private String userName;

    /** 状态 0成功 1失败 */
    @Excel(name = "状态", readConverterExp = "0=成功,1=失败")
    private String status;

    /** 地址 */
    @Excel(name = "地址")
    private String ipaddr;

    /** 描述 */
    @Excel(name = "描述")
    private String msg;

    /** 访问时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "访问时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date accessTime;
}