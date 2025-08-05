package com.financial.business.entity.dto.statistic;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 收支记录折线图数据
 * @author xinyi
 */
@Schema(description = "收支记录折线图数据")
@Data
@AllArgsConstructor
public class IncomeExpenseLineDataDTO {
  @Schema(description = "日期")
  private LocalDate date;

  @Schema(description = "类型")
  private String type;

  @Schema(description = "金额")
  private BigDecimal amount;
}
