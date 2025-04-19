package com.financial.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.financial.common.core.web.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDate;
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

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description="预算ID")
    private Long budgetId;

    @Schema(description="已使用金额")
    private BigDecimal usedAmount;

    @Schema(description="剩余金额")
    private BigDecimal remainingAmount;

    @Schema(description="使用率（百分比）")
    private BigDecimal usageRate;

    @Schema(description="状态代码")
    private Long statusCode;

    @Schema(description="开始日期")
    private LocalDate startDate;

    @Schema(description="结束日期")
    private LocalDate endDate;

}
