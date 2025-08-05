package com.financial.business.entity.dto;

import cn.idev.excel.annotation.ExcelProperty;
import com.financial.common.security.annotation.Dict;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
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

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "预算名称")
    private String name;

    @Schema(description = "预算类型")
    private String type;

    @Schema(description = "预算金额")
    private BigDecimal budgetAmount;

    @Schema(description = "预算周期")
    private String period;

    @Schema(description = "预警阈值（百分比）")
    private BigDecimal warningThreshold;

    @Schema(description = "是否结转")
    private Byte rollover;

    @Schema(description="是否启用")
    private Byte enabled;

    @Schema(description = "预算ID集合")
    private List<Long> ids;

    @Schema(description="已使用金额")
    private BigDecimal usedAmount;

    @Schema(description="使用率")
    private BigDecimal usageRate;

}