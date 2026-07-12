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
public class MonthlyReportDTO {
    private String month;

    private BigDecimal totalIncome;

    private BigDecimal totalExpense;

    private BigDecimal monthlySavings;

    private BigDecimal totalBudget;

    private BigDecimal remainingBudget;

    private String highestExpenseTitle;

    private BigDecimal highestExpenseAmount;

    private String highestIncomeSource;

    private BigDecimal highestIncomeAmount;

    private String mostSpentCategory;

    private String budgetStatus;
}
