package com.financial.business.entity.dto.data;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 基金申购状态
 * @author xinyi
 */
@Data
@Schema(description = "基金申购状态")
@AllArgsConstructor
@NoArgsConstructor
public class FundBuyStatus {
    @Schema(description = "基金代码")
    private String code;
    @Schema(description = "基金名称")
    private String name;
    @Schema(description = "基金最新净值/万份收益")
    private BigDecimal equity;
    @Schema(description = "最新净值/万份收益")
    private String time;
    @Schema(description = "申购状态")
    private String buyStatus;
    @Schema(description = "赎回状态")
    private String sellStatus;
    @Schema(description = "下一开放日")
    private String nextOpenTime;
    @Schema(description = "购买起点")
    private BigDecimal buyBegin;
    @Schema(description = "日累计限定金额")
    private BigDecimal buyEnd;
    @Schema(description = "购买手续费")
    private BigDecimal fee;
}
