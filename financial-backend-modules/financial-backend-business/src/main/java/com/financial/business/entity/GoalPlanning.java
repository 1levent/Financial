package com.financial.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.financial.common.core.web.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 目标设定表
 * </p>
 *
 * @author xinyi
 * @since 2025-03-28
 */
@EqualsAndHashCode(callSuper = true)
@TableName("goal_planning")
@Schema(description = "目标设定表")
@Data
public class GoalPlanning extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description="目标ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description="目标名称")
    private String name;

    @Schema(description="目标金额")
    private BigDecimal targetAmount;

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
