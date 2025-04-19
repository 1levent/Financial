package com.financial.business.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 预算跟踪数据传输对象
 * @author xinyi
 */
@Data
@Schema(description = "预算跟踪数据传输对象")
public class BudgetTrackingDTO {

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "预算ID")
    private Long budgetId;

    @Schema(description = "预算名称")
    private String name;

    @Schema(description = "预算类型编码")
    private Long typeCode;

    @Schema(description = "预算金额")
    private BigDecimal budgetAmount;

    @Schema(description = "已使用金额")
    private BigDecimal usedAmount;

    @Schema(description = "剩余金额")
    private BigDecimal remainingAmount;

    @Schema(description = "使用率（百分比）")
    private BigDecimal usageRate;

    @Schema(description = "状态代码")
    private Long statusCode;

    @Schema(description = "开始日期")
    private LocalDate startDate;

    @Schema(description = "结束日期")
    private LocalDate endDate;

}