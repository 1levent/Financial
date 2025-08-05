package com.financial.business.entity.dto.statistic;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 收支记录分类数据饼图
 * @author xinyi
 */
@Schema(description = "收支记录分类数据饼图")
@Data
@AllArgsConstructor
public class IncomeExpenseCategoryDataDTO {

  @Schema(description = "分类")
  private String category;

  @Schema(description = "类型")
  private String type;

  @Schema(description = "金额")
  private BigDecimal amount;

}
