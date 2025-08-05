package com.financial.business.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 借贷记录DTO
 * @author xinyi
 * @date 2025/3/29
 */

@Data
@Schema(description = "借贷记录DTO")
public class LoanRecordsDTO {

    @Schema(description="借贷记录ID")
    private Long id;

    @Schema(description="用户ID")
    private Long userId;

    @Schema(description="借贷名称")
    private String loanName;

    @Schema(description="借贷类型")
    private String type;

    @Schema(description="本金")
    private BigDecimal principal;

    @Schema(description="年利率")
    private BigDecimal annualInterestRate;

    @Schema(description="借款期限（单位：月）")
    private Integer loanTerm;

    @Schema(description="总利息")
    private BigDecimal totalInterest;

    @Schema(description="状态")
    private String status;
}