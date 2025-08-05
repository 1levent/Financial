package com.financial.business.entity.dto.statistic;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 还款计划柱状图
 * @author xinyi
 */
@Data
@Schema(description = "还款计划柱状图")
public class RepaymentBarDTO {
  @Schema(description = "借贷名称")
  private String name;
  @Schema(description = "总借贷")
  private BigDecimal total;
  @Schema(description = "已还款")
  private BigDecimal paid;
}
