package com.manasa.smartexpenseplatform.dto;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetAnalyticsDTO {
    private String category;

    private BigDecimal budget;

    private BigDecimal spent;

    private BigDecimal remaining;

    private Double percentageUsed;

    private String status;
}
