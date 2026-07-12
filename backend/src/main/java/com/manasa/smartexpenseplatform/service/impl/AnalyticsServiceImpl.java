package com.manasa.smartexpenseplatform.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.manasa.smartexpenseplatform.entity.Budget;
import com.manasa.smartexpenseplatform.entity.User;
import com.manasa.smartexpenseplatform.dto.BudgetAnalyticsDTO;
import com.manasa.smartexpenseplatform.repository.BudgetRepository;
import com.manasa.smartexpenseplatform.repository.ExpenseRepository;
import com.manasa.smartexpenseplatform.repository.UserRepository;
import com.manasa.smartexpenseplatform.service.AnalyticsService;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    private final UserRepository userRepository;
    private final BudgetRepository budgetRepository;
    private final ExpenseRepository expenseRepository;

    public AnalyticsServiceImpl(
            UserRepository userRepository,
            BudgetRepository budgetRepository,
            ExpenseRepository expenseRepository) {

        this.userRepository = userRepository;
        this.budgetRepository = budgetRepository;
        this.expenseRepository = expenseRepository;
    }
    @Override
    public List<BudgetAnalyticsDTO> getBudgetAnalytics() {

    // Get logged-in user
        Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

    // Get all budgets of the user
        List<Budget> budgets = budgetRepository.findByUser(user);

    // List to store analytics
        List<BudgetAnalyticsDTO> analyticsList = new ArrayList<>();

    // Loop through each budget
        for (Budget budget : budgets) {

        // Total spent for this category
            BigDecimal spent = expenseRepository.getTotalExpenseByUserAndCategory(
                user,
                budget.getExpenseCategory());

        // Remaining amount
            BigDecimal remaining = budget.getBudgetAmount().subtract(spent);

        // Percentage used
            Double percentageUsed = 0.0;

            if (budget.getBudgetAmount().compareTo(BigDecimal.ZERO) > 0) {

            percentageUsed = spent
                    .multiply(BigDecimal.valueOf(100))
                    .divide(budget.getBudgetAmount(), 2, RoundingMode.HALF_UP)
                    .doubleValue();
            }

        // Budget status
            String status;

            if (spent.compareTo(budget.getBudgetAmount()) <= 0) {
            status = "Within Budget";
            } else {
            status = "Exceeded";
            }

        // Create DTO
            BudgetAnalyticsDTO dto = BudgetAnalyticsDTO.builder()
                .category(budget.getExpenseCategory().getName())
                .budget(budget.getBudgetAmount())
                .spent(spent)
                .remaining(remaining)
                .percentageUsed(percentageUsed)
                .status(status)
                .build();

                analyticsList.add(dto);
            }

        return analyticsList;
    }

}