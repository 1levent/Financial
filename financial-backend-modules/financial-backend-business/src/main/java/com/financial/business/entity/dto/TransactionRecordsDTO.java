package com.financial.business.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description="资产代码")
    private String assetCode;

    @Schema(description="交易类型编码")
    private Long typeCode;

    @Schema(description="交易份额")
    private BigDecimal shares;

    @Schema(description="交易价格")
    private BigDecimal price;

    @Schema(description="交易费用")
    private BigDecimal fee;

    @Schema(description="交易时间")
    private LocalDateTime transactionTime;
}