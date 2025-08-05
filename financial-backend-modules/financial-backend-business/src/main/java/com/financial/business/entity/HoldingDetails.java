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
 * 持仓明细表
 * </p>
 *
 * @author xinyi
 * @since 2025-03-28
 */
@EqualsAndHashCode(callSuper = true)
@TableName("holding_details")
@Schema(description = "持仓明细表")
@Data
public class HoldingDetails extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description="ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description="用户ID")
    private Long userId;

    @Schema(description="账户ID")
    private String accountNo;

    @Schema(description="代码")
    private String code;

    @Schema(description="名称")
    private String name;

    @Schema(description="类型")
    private String type;

    @Schema(description="持仓金额")
    private BigDecimal quantity;

    @Schema(description="成本")
    private BigDecimal cost;

    @Schema(description="盈亏")
    private BigDecimal profitLoss;

    @Schema(description="收益率")
    private BigDecimal returnRate;

    @Schema(description="持有起始日期")
    private LocalDate startDate;

    @Schema(description="持有结束日期")
    private LocalDate endDate;

    @Schema(description="持有份额")
    private BigDecimal shares;

    @Schema(description="当前净值")
    private BigDecimal equity;

    @Schema(description="单位成本")
    private BigDecimal unitCost;
}
