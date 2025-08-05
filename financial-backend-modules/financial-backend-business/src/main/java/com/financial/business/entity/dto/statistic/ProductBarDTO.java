package com.financial.business.entity.dto.statistic;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 产品柱状图
 * @author xinyi
 */
@Data
@Schema(description = "产品柱状图")
public class ProductBarDTO {
  @Schema(description = "产品名称")
  private String name;
  @Schema(description = "产品持仓金额")
  private Double total;
}
