package com.financial.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.financial.common.core.web.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 收益记录表
 * </p>
 *
 * @author xinyi
 * @since 2025-03-28
 */
@EqualsAndHashCode(callSuper = true)
@TableName("profit_records")
@Schema(description = "收益记录表")
@Data
public class ProfitRecords extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description="收益ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description="用户ID")
    private Long userId;

    @Schema(description="持仓明细ID")
    private Long holdingDetailId;

    @Schema(description="收益类型编码")
    private Long typeCode;

    @Schema(description="收益金额")
    private BigDecimal profitAmount;

    @Schema(description="收益时间")
    private LocalDateTime profitTime;
}
