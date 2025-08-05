package com.financial.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.financial.common.core.web.domain.BaseEntity;
import com.financial.common.security.annotation.Dict;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 收支记录表
 * </p>
 *
 * @author xinyi
 * @since 2025-03-28
 */
@EqualsAndHashCode(callSuper = true)
@TableName("income_expense_records")
@Schema(description = "收支记录表")
@Data
public class IncomeExpenseRecords extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description="记录ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description="用户ID")
    private Long userId;

    @Schema(description="账户ID")
    private String accountId;

    @Schema(description="预算ID")
    private String budgetId;

    @Schema(description="类型")
    private String type;

    @Schema(description="金额")
    private BigDecimal amount;

    @Schema(description="分类")
    private String category;

    @Schema(description="日期")
    private LocalDate date;

    @Schema(description="描述")
    private String description;

    @Schema(description="借贷记录ID")
    private Long loanId;

    @Schema(description="目标ID")
    private Long goalId;

    @Schema(description="逻辑删除")
    private boolean deleted;

}
