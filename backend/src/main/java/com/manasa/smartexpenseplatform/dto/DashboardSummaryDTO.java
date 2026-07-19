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
@Schema(description = "Response DTO containing the dashboard financial summary")
public class DashboardSummaryDTO {

    @Schema(
            description = "Total income earned by the authenticated user",
            example = "50000.00"
    )
    private BigDecimal totalIncome;

    @Schema(
            description = "Total expenses incurred by the authenticated user",
            example = "18500.00"
    )
    private BigDecimal totalExpense;

    @Schema(
            description = "Current balance calculated as Total Income - Total Expense",
            example = "31500.00"
    )
    private BigDecimal currentBalance;

    @Schema(
            description = "Total budget allocated by the authenticated user",
            example = "25000.00"
    )
    private BigDecimal totalBudget;

    @Schema(
            description = "Remaining budget after deducting expenses from the allocated budget",
            example = "6500.00"
    )
    private BigDecimal remainingBudget;
}