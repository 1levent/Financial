package com.financial.business.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 收益记录DTO
 * @author xinyi
 * @date 2025/3/29
 */

@Data
@Schema(description = "收益记录DTO")
public class ProfitRecordsDTO {

    @Schema(description="收益ID")
    private Long id;

    @Schema(description="用户ID")
    private Long userId;

    @Schema(description="持仓明细ID")
    private Long holdingDetailId;

    @Schema(description="收益类型编码")
    private Long typeCode;

    @Schema(description="收益金额")
    private BigDecimal profitAmount;

    @Schema(description="收益时间")
    private LocalDateTime profitTime;
}