package com.financial.business.entity.dto.statistic;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 账户饼图
 * @author xinyi
 */
@Data
@Schema(description = "账户饼图")
public class AccountPieDTO {
  @Schema(description = "账户编号")
  private String accountNo;
  @Schema(description = "账户类型")
  private String type;
  @Schema(description = "账户余额")
  private Double amount;
}
