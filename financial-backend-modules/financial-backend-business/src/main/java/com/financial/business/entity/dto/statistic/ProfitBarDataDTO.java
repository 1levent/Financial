package com.financial.business.entity.dto.statistic;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 收益条形图
 * @author xinti
 */
@Data
public class ProfitBarDataDTO {
  @Schema(description = "产品名称")
  private String name;

  @Schema(description = "收益")
  private BigDecimal value;
}
