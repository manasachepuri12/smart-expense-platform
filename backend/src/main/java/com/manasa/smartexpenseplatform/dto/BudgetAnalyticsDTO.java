package com.manasa.smartexpenseplatform.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Response DTO containing budget analytics for an expense category")
public class BudgetAnalyticsDTO {

    @Schema(
            description = "Name of the expense category",
            example = "Food"
    )
    private String category;

    @Schema(
            description = "Allocated budget for the category",
            example = "10000.00"
    )
    private BigDecimal budget;

    @Schema(
            description = "Total amount spent in the category",
            example = "6500.00"
    )
    private BigDecimal spent;

    @Schema(
            description = "Remaining budget for the category",
            example = "3500.00"
    )
    private BigDecimal remaining;

    @Schema(
            description = "Percentage of the budget that has been used",
            example = "65.0"
    )
    private Double percentageUsed;

    @Schema(
            description = "Current budget status",
            example = "Within Budget"
    )
    private String status;
}