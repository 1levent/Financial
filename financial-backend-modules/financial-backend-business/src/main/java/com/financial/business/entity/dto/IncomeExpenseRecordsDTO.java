package com.financial.business.entity.dto;

import com.financial.common.security.annotation.Dict;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 收支记录DTO
 * @author xinyi
 * @date 2025/3/29
 */

@Data
@Schema(description = "收支记录DTO")
public class IncomeExpenseRecordsDTO {

    @Schema(description="记录ID")
    private Long id;

    @Schema(description="用户ID")
    private Long userId;

    @Schema(description="类型编码")
    @Dict(type = "income_expense_type")
    private Long typeCode;

    @Schema(description="金额")
    private BigDecimal amount;

    //fixme 这里需要根据收支的类型来确定分类的类型
    @Schema(description="分类")
    @Dict(type = "transaction_category_income")
    private Long categoryCode;

    @Schema(description="日期")
    private LocalDate date;

    @Schema(description="描述")
    private String description;

    @Schema(description="开始日期")
    private LocalDate startDate;

    @Schema(description="结束日期")
    private LocalDate endDate;

}