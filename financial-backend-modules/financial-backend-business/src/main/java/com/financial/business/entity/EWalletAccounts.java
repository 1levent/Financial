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
 * 电子钱包账户表
 * </p>
 *
 * @author xinyi
 * @since 2025-03-28
 */
@EqualsAndHashCode(callSuper = true)
@TableName("e_wallet_accounts")
@Schema(description = "电子钱包账户表")
@Data
public class EWalletAccounts extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description="钱包ID")
    @TableId(value = "id", type = IdType.AUTO)
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
