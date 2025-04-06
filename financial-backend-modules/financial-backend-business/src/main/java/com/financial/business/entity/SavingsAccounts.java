package com.financial.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.financial.common.core.web.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 储蓄卡账户表
 * </p>
 *
 * @author xinyi
 * @since 2025-03-28
 */
@EqualsAndHashCode(callSuper = true)
@TableName("savings_accounts")
@Schema(description = "储蓄卡账户表")
@Data
public class SavingsAccounts extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description="账户ID")
    @TableId(value = "id", type = IdType.AUTO)
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
