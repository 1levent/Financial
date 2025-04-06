package com.financial.business.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 信用卡账户数据传输对象
 * @author xinyi
 */
@Data
@Schema(description = "信用卡账户DTO")
public class CreditCardAccountsDTO {

    @Schema(description="信用卡ID")
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