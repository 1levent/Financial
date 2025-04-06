package com.financial.business.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 持仓明细DTO
 * @author xinyi
 * @date 2025/3/29
 */

@Data
@Schema(description = "持仓明细DTO")
public class HoldingDetailsDTO {

    @Schema(description="持仓明细ID")
    private Long id;

    @Schema(description="用户ID")
    private Long userId;

    @Schema(description="持仓明细代码")
    private String code;

    @Schema(description="名称")
    private String name;

    @Schema(description="类型编码")
    private Long typeCode;

    @Schema(description="持仓金额")
    private BigDecimal quantity;

    @Schema(description="成本")
    private BigDecimal cost;

    @Schema(description="市值")
    private BigDecimal marketValue;

    @Schema(description="盈亏")
    private BigDecimal profitLoss;

    @Schema(description="收益率")
    private BigDecimal returnRate;

    @Schema(description="持有起始日期")
    private LocalDate startDate;

    @Schema(description="持有结束日期")
    private LocalDate endDate;
}