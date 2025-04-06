package com.financial.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.financial.common.core.web.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 交易记录表
 * </p>
 *
 * @author xinyi
 * @since 2025-03-28
 */
@EqualsAndHashCode(callSuper = true)
@TableName("transaction_records")
@Schema(description = "交易记录表")
@Data
public class TransactionRecords extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description="交易ID")
    @TableId(value = "id", type = IdType.AUTO)
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
