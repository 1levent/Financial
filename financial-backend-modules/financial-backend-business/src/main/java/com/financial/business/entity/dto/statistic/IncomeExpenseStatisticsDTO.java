package com.financial.business.entity.dto.statistic;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

/**
 * 收入支出相关统计
 * @author xinyi
 */
@Data
@Schema(description = "收入支出相关数据统计")
public class IncomeExpenseStatisticsDTO {

  @Schema(description = "总收入")
  private BigDecimal incomeTotal;

  @Schema(description = "总支出")
  private BigDecimal expenseTotal;

  @Schema(description = "结余")
  private BigDecimal remain;

  @Schema(description = "收支趋势")
  private List<IncomeExpenseLineDataDTO> lineData;

  @Schema(description = "收入分类数据")
  private List<IncomeExpenseCategoryDataDTO> categoryData;
}
