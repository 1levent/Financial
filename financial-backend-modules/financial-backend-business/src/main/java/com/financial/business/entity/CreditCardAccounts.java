package com.financial.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.financial.common.core.web.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 信用卡账户表
 * </p>
 *
 * @author xinyi
 * @since 2025-03-28
 */
@EqualsAndHashCode(callSuper = true)
@TableName("credit_card_accounts")
@Schema(description = "信用卡账户表")
@Data
public class CreditCardAccounts extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description="信用卡ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description="用户ID")
    private Long userId;

    @Schema(description="发卡行")
    private String issuingBank;

    @Schema(description="卡片类型编码")
    private Long typeCode;

    @Schema(description="卡号")
    private String cardNumber;

    @Schema(description="信用额度")
    private BigDecimal creditLimit;

    @Schema(description="已用额度")
    private BigDecimal usedCredit;

    @Schema(description="可用额度")
    private BigDecimal availableCredit;

    @Schema(description="账单日")
    private Byte billingDay;

    @Schema(description="还款日")
    private Byte paymentDueDay;

    @Schema(description="当期账单")
    private BigDecimal currentBill;

    @Schema(description="账户状态代码")
    private Long statusCode;
}
