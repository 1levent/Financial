package com.financial.business.entity.dto.statistic;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 借贷统计数据
 * @author xinyi
 */
@Data
@Schema(description = "借贷统计数据")
public class LoanStatisticDTO {

    @Schema(description = "借贷总数")
    private Integer totalCount;

    @Schema(description = "总借贷金额")
    private BigDecimal totalBorrowed;

    @Schema(description = "总利息")
    private BigDecimal totalInterest;

}
