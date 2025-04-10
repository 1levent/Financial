package com.financial.business.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 目标设定数据传输对象
 * @author xinyi
 * @date 2025/3/29
 */

@Data
@Schema(description = "目标设定DTO")
public class GoalPlanningDTO {

    @Schema(description="目标ID")
    private Long id;

    @Schema(description="目标名称")
    private String name;

    @Schema(description="目标类型编码")
    private Long goalTypeCode;

    @Schema(description="目标金额")
    private BigDecimal targetAmount;

    @Schema(description="当前金额")
    private BigDecimal currentAmount;

    @Schema(description="开始日期")
    private LocalDate startDate;

    @Schema(description="结束日期")
    private LocalDate endDate;

    @Schema(description="状态代码")
    private Long statusCode;

    @Schema(description="优先级代码")
    private Long priorityCode;

    @Schema(description="描述")
    private String description;
}