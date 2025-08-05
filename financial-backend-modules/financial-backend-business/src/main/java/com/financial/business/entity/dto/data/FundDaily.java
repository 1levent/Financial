package com.financial.business.entity.dto.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 基金每日净值
 * @author xinyi
 */
@Data
@AllArgsConstructor
public class FundDaily {
    @JsonProperty("基金代码")
    private String fundCode;

    @JsonProperty("日增长率")
    private String dailyGrowthRate;
}
