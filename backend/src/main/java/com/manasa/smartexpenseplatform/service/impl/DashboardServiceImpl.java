package com.manasa.smartexpenseplatform.service.impl;

import java.math.BigDecimal;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.manasa.smartexpenseplatform.dto.DashboardSummaryDTO;
import com.manasa.smartexpenseplatform.entity.User;
import com.manasa.smartexpenseplatform.exception.ResourceNotFoundException;
import com.manasa.smartexpenseplatform.repository.BudgetRepository;
import com.manasa.smartexpenseplatform.repository.ExpenseRepository;
import com.manasa.smartexpenseplatform.repository.IncomeRepository;
import com.manasa.smartexpenseplatform.repository.UserRepository;
import com.manasa.smartexpenseplatform.service.DashboardService;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final UserRepository userRepository;
    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;
    private final BudgetRepository budgetRepository;

    public DashboardServiceImpl(
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
    public DashboardSummaryDTO getDashboardSummary() {

        // Get logged-in user
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        // Fetch totals
        BigDecimal totalIncome = incomeRepository.getTotalIncomeByUser(user);
        BigDecimal totalExpense = expenseRepository.getTotalExpenseByUser(user);
        BigDecimal totalBudget = budgetRepository.getTotalBudgetByUser(user);

        // Perform calculations
        BigDecimal currentBalance = totalIncome.subtract(totalExpense);
        BigDecimal remainingBudget = totalBudget.subtract(totalExpense);

        // Return DTO
        return DashboardSummaryDTO.builder()
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .currentBalance(currentBalance)
                .totalBudget(totalBudget)
                .remainingBudget(remainingBudget)
                .build();
    }
}