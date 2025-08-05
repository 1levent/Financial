package com.financial.business.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression.DateTime;

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

    @Schema(description="涨跌幅")
    private BigDecimal chg;

    @Schema(description="收益金额")
    private BigDecimal profitAmount;

    @Schema(description="收益时间")
    private Date profitTime;
}