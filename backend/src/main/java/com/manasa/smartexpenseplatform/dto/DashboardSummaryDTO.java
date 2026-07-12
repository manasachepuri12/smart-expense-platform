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
public class DashboardSummaryDTO {

    private BigDecimal totalIncome;

    private BigDecimal totalExpense;

    private BigDecimal currentBalance;

    private BigDecimal totalBudget;

    private BigDecimal remainingBudget;
}