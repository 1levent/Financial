package com.financial.business.entity.dto;

import cn.idev.excel.annotation.ExcelIgnore;
import cn.idev.excel.annotation.ExcelProperty;
import com.financial.common.core.utils.excel.ExcelDateConverter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
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

    @ExcelProperty(value = "记录ID")
    @Schema(description = "记录ID")
    private Long id;

    @ExcelIgnore
    @Schema(description = "用户ID")
    private Long userId;

    @ExcelProperty(value = "账户ID")
    @Schema(description="账户ID")
    private String accountId;

    @Schema(description="预算ID")
    private String budgetId;

    @ExcelProperty(value = "类型")
    @Schema(description = "类型")
    private String type;

    @ExcelProperty(value = "金额")
    @Schema(description = "金额")
    private BigDecimal amount;

    @ExcelProperty(value = "分类")
    //fixme 这里需要根据收支的类型来确定分类的类型
    @Schema(description = "分类")
    private String category;

    @ExcelProperty(value = "日期",converter = ExcelDateConverter.class)
    @Schema(description = "日期")
    private LocalDate date;


    @ExcelProperty(value = "描述")
    @Schema(description = "描述")
    private String description;

    @ExcelIgnore
    @Schema(description = "开始日期")
    private LocalDate startDate;

    @ExcelIgnore
    @Schema(description = "结束日期")
    private LocalDate endDate;


    @ExcelIgnore
    @Schema(description="借贷记录ID")
    private Long loanId;

    @ExcelIgnore
    @Schema(description="目标ID")
    private Long goalId;

    @ExcelIgnore
    @Schema(description = "记录ID集合")
    private List<Long> ids;

}