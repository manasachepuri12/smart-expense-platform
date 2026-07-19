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
@Schema(description = "Response DTO containing the monthly financial report of the authenticated user")
public class MonthlyReportDTO {

    @Schema(
            description = "Month for which the report is generated",
            example = "July 2026"
    )
    private String month;

    @Schema(
            description = "Total income received during the month",
            example = "50000.00"
    )
    private BigDecimal totalIncome;

    @Schema(
            description = "Total expenses incurred during the month",
            example = "32000.00"
    )
    private BigDecimal totalExpense;

    @Schema(
            description = "Monthly savings calculated as Total Income - Total Expense",
            example = "18000.00"
    )
    private BigDecimal monthlySavings;

    @Schema(
            description = "Total budget allocated for the month",
            example = "35000.00"
    )
    private BigDecimal totalBudget;

    @Schema(
            description = "Remaining budget after deducting monthly expenses",
            example = "3000.00"
    )
    private BigDecimal remainingBudget;

    @Schema(
            description = "Title of the highest expense recorded during the month",
            example = "Laptop Purchase"
    )
    private String highestExpenseTitle;

    @Schema(
            description = "Amount of the highest expense recorded during the month",
            example = "45000.00"
    )
    private BigDecimal highestExpenseAmount;

    @Schema(
            description = "Source of the highest income received during the month",
            example = "Salary"
    )
    private String highestIncomeSource;

    @Schema(
            description = "Amount of the highest income received during the month",
            example = "50000.00"
    )
    private BigDecimal highestIncomeAmount;

    @Schema(
            description = "Expense category with the highest spending during the month",
            example = "Food"
    )
    private String mostSpentCategory;

    @Schema(
            description = "Overall budget status for the month",
            example = "Within Budget"
    )
    private String budgetStatus;
}