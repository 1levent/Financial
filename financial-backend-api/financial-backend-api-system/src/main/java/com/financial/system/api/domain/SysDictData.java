package com.financial.system.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serial;
import lombok.Data;
import com.financial.common.core.web.domain.BaseEntity;
import lombok.EqualsAndHashCode;

/**
 * 字典数据表 sys_dict_data
 * 
 * @author xinyi
 */
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dict_data")
@Data
public class SysDictData extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    /** 字典编码 */
    @TableId(type = IdType.AUTO)
    private Long dictCode;

    /** 字典排序 */
    private Long dictSort;

    /** 字典标签 */
    private String dictLabel;

    /** 字典键值 */
    private String dictValue;

    /** 字典类型 */
    private String dictType;

    /** 样式属性（其他样式扩展） */
    private String cssClass;

    /** 表格字典样式 */
    private String listClass;

    /** 是否默认（Y是 N否） */
    private String isDefault;

    /** 状态（0正常 1停用） */
    private String status;

}
