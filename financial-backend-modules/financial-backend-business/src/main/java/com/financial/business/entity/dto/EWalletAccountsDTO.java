package com.financial.business.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 电子钱包账户DTO
 * @author xinyi
 */
@Data
@Schema(description = "电子钱包账户DTO")
public class EWalletAccountsDTO {

    @Schema(description="钱包ID")
    private Long id;

    @Schema(description="用户ID")
    private Long userId;

    @Schema(description="钱包名称")
    private String walletName;

    @Schema(description="余额")
    private BigDecimal balance;

    @Schema(description="账户状态代码")
    private Long statusCode;

    @Schema(description="最后交易时间")
    private LocalDateTime lastTransactionTime;
}