package com.financial.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.financial.common.core.web.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 还款计划表
 * </p>
 *
 * @author xinyi
 * @since 2025-03-28
 */
@EqualsAndHashCode(callSuper = true)
@TableName("repayment_plans")
@Schema(description = "还款计划表")
@Data
public class RepaymentPlans extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description="还款计划ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description="用户ID")
    private Long userId;

    @Schema(description="账户ID")
    private String accountId;

    @Schema(description="借贷记录ID")
    private Long loanRecordId;

    @Schema(description="还款日期")
    private LocalDate repaymentDate;

    @Schema(description="还款金额")
    private BigDecimal repaymentAmount;

    @Schema(description="还款进度（百分比）")
    private BigDecimal repaymentProgress;

    @Schema(description="还款总额")
    private BigDecimal repaymentTotalAmount;

    @Schema(description="还款周期")
    private String period;

    @Schema(description="还款次数")
    private Long count;

    @Schema(description="状态")
    private String status;

    @Schema(description="任务ID")
    private Integer jobId;
}
