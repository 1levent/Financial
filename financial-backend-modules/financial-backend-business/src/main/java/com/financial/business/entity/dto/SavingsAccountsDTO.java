package com.financial.business.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 储蓄卡账户DTO
 * @author xinyi
 * @date 2025/3/29
 */

@Data
@Schema(description = "储蓄卡账户DTO")
public class SavingsAccountsDTO {

    @Schema(description="账户ID")
    private Long id;

    @Schema(description="用户ID")
    private Long userId;

    @Schema(description="开户行")
    private String bankName;

    @Schema(description="账户类型编码")
    private Long typeCode;

    @Schema(description="账号")
    private String accountNumber;

    @Schema(description="余额")
    private BigDecimal balance;

    @Schema(description="利率")
    private BigDecimal interestRate;

    @Schema(description="到期日")
    private LocalDate maturityDate;

    @Schema(description="账户状态代码")
    private Long statusCode;

    @Schema(description="最后交易时间")
    private LocalDateTime lastTransactionTime;
}