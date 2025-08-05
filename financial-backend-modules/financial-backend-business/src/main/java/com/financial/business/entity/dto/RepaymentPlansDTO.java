package com.financial.business.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 还款计划DTO
 * @author xinyi
 */
@Data
@Schema(description = "还款计划DTO")
public class RepaymentPlansDTO {

    @Schema(description="还款计划ID")
    private Long id;

    @Schema(description="用户ID")
    private Long userId;

    @Schema(description="账户ID")
    private String accountId;

    @Schema(description="借贷记录ID")
    private Long loanRecordId;

    @Schema(description="借贷名称")
    private String loanName;

    @Schema(description="还款日期")
    private LocalDate repaymentDate;

    @Schema(description="还款金额")
    private BigDecimal repaymentAmount;

    @Schema(description="还款进度（百分比）")
    private BigDecimal repaymentProgress;

    @Schema(description="还款总额")
    private BigDecimal repaymentTotalAmount;

    @Schema(description="还款周期")
    private String period;

    @Schema(description="还款次数")
    private Long count;

    @Schema(description="状态")
    private String status;
}