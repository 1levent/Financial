package com.financial.business.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.Date;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 交易记录DTO
 * @author xinyi
 * @date 2025/3/29
 */

@Data
@Schema(description = "交易记录DTO")
public class TransactionRecordsDTO {

    @Schema(description="交易ID")
    private Long id;

    @Schema(description="用户ID")
    private Long userId;

    @Schema(description="账户编号")
    private String accountNo;

    @Schema(description="资产代码")
    private String assetCode;

    @Schema(description="交易类型")
    private String type;

    @Schema(description="交易份额")
    private BigDecimal shares;

    @Schema(description="交易价格")
    private BigDecimal price;

    @Schema(description="交易费用")
    private BigDecimal fee;

    @Schema(description="交易时间")
    private LocalDate transactionTime;

    @Schema(description="是否在3点前的交易")
    private boolean isBeforeThree;
}