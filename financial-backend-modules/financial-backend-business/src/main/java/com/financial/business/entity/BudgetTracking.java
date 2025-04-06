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
 * 预算跟踪表
 * </p>
 *
 * @author xinyi
 * @since 2025-03-28
 */
@EqualsAndHashCode(callSuper = true)
@TableName("budget_tracking")
@Schema(description = "预算跟踪表")
@Data
public class BudgetTracking extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description="预算跟踪ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description="预算名称")
    private String name;

    @Schema(description="预算类型编码")
    private Long budgetTypeCode;

    @Schema(description="预算金额")
    private BigDecimal budgetAmount;

    @Schema(description="已使用金额")
    private BigDecimal usedAmount;

    @Schema(description="剩余金额")
    private BigDecimal remainingAmount;

    @Schema(description="使用率（百分比）")
    private BigDecimal usageRate;

    @Schema(description="状态代码")
    private Long statusCode;

    @Schema(description="周期代码")
    private Long periodCode;

}
