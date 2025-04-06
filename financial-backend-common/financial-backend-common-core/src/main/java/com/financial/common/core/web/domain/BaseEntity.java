package com.financial.common.core.web.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;
import lombok.Setter;

/**
 * Entity基类
 * 
 * @author xinyi
 */
@Data
public class BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /** 搜索值 */
    @JsonIgnore
    @TableField(exist = false)
    private String searchValue;

    /** 创建者 */
    @TableField(value = "create_by",jdbcType = JdbcType.VARCHAR)
    private String createBy;

    /** 创建时间 */
    @TableField(value = "create_time",jdbcType = JdbcType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新者 */
    @TableField(value = "update_by",jdbcType = JdbcType.VARCHAR)
    private String updateBy;

    /** 更新时间 */
    @TableField(value = "update_time",jdbcType = JdbcType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /** 备注 */
    @TableField(value = "remark",jdbcType = JdbcType.VARCHAR)
    private String remark;

    /** 请求参数 */
    @TableField(exist = false)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, Object> params;
}
