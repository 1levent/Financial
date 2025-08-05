package com.financial.business.entity.dto.statistic;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 产品收益图
 * @author xinyi
 */
@Data
@Schema(description = "产品收益图")
public class ProductProfitDTO {
  @Schema(description = "产品名称")
  private String name;
  @Schema(description = "产品收益")
  private BigDecimal total;

}
