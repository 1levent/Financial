package com.financial.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.financial.common.core.annotation.Excel;
import com.financial.common.core.annotation.Excel.ColumnType;
import com.financial.common.core.web.domain.BaseEntity;

/**
 * 参数配置表 sys_config
 * 
 * @author xinyi
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_config")
@Schema(name = "参数配置表")
public class SysConfig extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    /** 参数主键 */
    @TableId(type = IdType.AUTO)
    @Excel(name = "参数主键", cellType = ColumnType.NUMERIC)
    private Long configId;

    /** 参数名称 */
    @Excel(name = "参数名称")
    private String configName;

    /** 参数键名 */
    @Excel(name = "参数键名")
    private String configKey;

    /** 参数键值 */
    @Excel(name = "参数键值")
    private String configValue;

    /** 系统内置（Y是 N否） */
    @Excel(name = "系统内置", readConverterExp = "Y=是,N=否")
    private String configType;
}
