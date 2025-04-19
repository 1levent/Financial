package com.financial.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.financial.common.core.web.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 预算管理表
 * </p>
 *
 * @author xinyi
 * @since 2025-03-28
 */
@EqualsAndHashCode(callSuper = true)
@TableName("budget_management")
@Schema(description = "预算管理表")
@Data
public class BudgetManagement extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "预算ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description="预算名称")
    private String name;

    @Schema(description="预算类型编码")
    private Long typeCode;

    @Schema(description="预算金额")
    private BigDecimal budgetAmount;

    @Schema(description="预算周期")
    private String periodCode;

    @Schema(description="预警阈值（百分比）")
    private BigDecimal warningThreshold;

    @Schema(description="是否结转")
    private Byte rollover;

    @Schema(description="是否启用")
    private Byte enabled;

}
