package com.financial.business.entity.dto;

import com.financial.common.security.annotation.Dict;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 预算管理数据传输对象
 * @author xinyi
 */
@Data
@Schema(description = "预算管理数据传输对象")
public class BudgetManagementDTO {

    @Schema(description = "预算ID")
    private Long id;

    @Schema(description = "预算名称")
    private String name;

    @Schema(description = "预算类型编码")
    @Dict(type = "budget_type")
    private Long typeCode;

    @Schema(description = "预算金额")
    private BigDecimal budgetAmount;

    @Schema(description = "预算周期")
    private String budgetPeriod;

    @Schema(description = "预警阈值（百分比）")
    private BigDecimal warningThreshold;

    @Schema(description = "是否结转")
    private Byte rollover;

    @Schema(description = "状态代码")
    private Long statusCode;

}