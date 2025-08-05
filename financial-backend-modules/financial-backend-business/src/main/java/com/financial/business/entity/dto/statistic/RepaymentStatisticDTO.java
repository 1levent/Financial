package com.financial.business.entity.dto.statistic;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 本月还款数据
 * @author xinyi
 */
@Data
public class RepaymentStatisticDTO {

  @Schema(description = "本月金额")
  private BigDecimal repaymentAmountByMonth;

  @Schema(description = "本月本金")
  private BigDecimal repaymentCost;

  @Schema(description = "本月利息")
  private BigDecimal repaymentInterest;
}
