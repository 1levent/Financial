package com.financial.business.entity.dto.statistic;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 产品饼图
 * @author xinyi
 */
@Data
@Schema(description = "产品饼图")
public class ProductPieDTO {
  @Schema(description = "产品类型")
  private String type;
  @Schema(description = "产品持仓金额")
  private Double total;
}
