package com.financial.business.entity.dto.statistic;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * @author Le vent
 */
@Data
@Schema(description = "收益日数据")
public class ProfitDayDataDTO {
  @Schema(description = "日期")
  private String date;
  @Schema(description = "收益")
  private BigDecimal value;
}
