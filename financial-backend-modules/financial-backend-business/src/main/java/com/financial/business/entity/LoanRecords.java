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
 * 借贷记录表
 * </p>
 *
 * @author xinyi
 * @since 2025-03-28
 */
@EqualsAndHashCode(callSuper = true)
@TableName("loan_records")
@Schema(description = "借贷记录表")
@Data
public class LoanRecords extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description="借贷记录ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description="用户ID")
    private Long userId;

    @Schema(description="借贷名称")
    private String loanName;

    @Schema(description="借贷类型")
    private String type;

    @Schema(description="本金")
    private BigDecimal principal;

    @Schema(description="年利率")
    private BigDecimal annualInterestRate;

    @Schema(description="借款期限（单位：月）")
    private Integer loanTerm;

    @Schema(description="总利息")
    private BigDecimal totalInterest;

    @Schema(description="状态")
    private String status;

}
