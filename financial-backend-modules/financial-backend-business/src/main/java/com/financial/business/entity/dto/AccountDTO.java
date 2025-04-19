package com.financial.business.entity.dto;

import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.Data;

/**
 * @author xinyi
 */
@Data
public class AccountDTO {
  @ExcelProperty(value = "主键")
  @Schema(description = "主键")
  private Long id;

  @ExcelProperty(value = "用户id")
  @Schema(description = "用户id")
  private Long userId;

  @ExcelProperty(value = "账户号")
  @Schema(description = "账户号")
  private String accountNo;

  @ExcelProperty(value = "账户类型")
  @Schema(description = "账户类型")
  private String accountType;

  @ExcelProperty(value = "所属机构")
  @Schema(description = "所属机构")
  private String institution;

  @ExcelProperty(value = "资产")
  @Schema(description = "资产")
  private BigDecimal amount;

  @ExcelProperty(value = "备注")
  @Schema(description = "备注")
  private String remark;

}
