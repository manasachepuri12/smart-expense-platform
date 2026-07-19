package com.manasa.smartexpenseplatform.service.impl;

import java.math.BigDecimal;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.manasa.smartexpenseplatform.dto.MonthlyReportDTO;
import com.manasa.smartexpenseplatform.entity.User;
import com.manasa.smartexpenseplatform.exception.ResourceNotFoundException;
import com.manasa.smartexpenseplatform.repository.BudgetRepository;
import com.manasa.smartexpenseplatform.repository.ExpenseRepository;
import com.manasa.smartexpenseplatform.repository.IncomeRepository;
import com.manasa.smartexpenseplatform.repository.UserRepository;
import com.manasa.smartexpenseplatform.service.ReportService;

@Service
public class ReportServiceImpl implements ReportService {

    private final UserRepository userRepository;
    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;
    private final BudgetRepository budgetRepository;

    public ReportServiceImpl(
            UserRepository userRepository,
            IncomeRepository incomeRepository,
            ExpenseRepository expenseRepository,
            BudgetRepository budgetRepository) {

        this.userRepository = userRepository;
        this.incomeRepository = incomeRepository;
        this.expenseRepository = expenseRepository;
        this.budgetRepository = budgetRepository;
    }

    @Override
    public MonthlyReportDTO getMonthlyReport(Integer month, Integer year) {

        // Get logged-in user
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        // Fetch monthly totals
        BigDecimal totalIncome =
                incomeRepository.getTotalIncomeByUserAndMonth(user, month, year);

        BigDecimal totalExpense =
                expenseRepository.getTotalExpenseByUserAndMonth(user, month, year);

        BigDecimal totalBudget =
                budgetRepository.getTotalBudgetByUserAndMonth(user, month, year);

        // Calculate values
        BigDecimal monthlySavings =
                totalIncome.subtract(totalExpense);

        BigDecimal remainingBudget =
                totalBudget.subtract(totalExpense);

        String budgetStatus;

        if (totalExpense.compareTo(totalBudget) <= 0) {
            budgetStatus = "Within Budget";
        } else {
            budgetStatus = "Exceeded";
        }

        // Build DTO
        return MonthlyReportDTO.builder()
                .month(month + "/" + year)
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .monthlySavings(monthlySavings)
                .totalBudget(totalBudget)
                .remainingBudget(remainingBudget)
                .budgetStatus(budgetStatus)
                .build();
    }
}