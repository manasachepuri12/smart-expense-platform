package com.manasa.smartexpenseplatform.mapper;

import com.manasa.smartexpenseplatform.dto.BudgetRequestDTO;
import com.manasa.smartexpenseplatform.dto.BudgetResponseDTO;
import com.manasa.smartexpenseplatform.entity.Budget;
import com.manasa.smartexpenseplatform.entity.ExpenseCategory;
import com.manasa.smartexpenseplatform.entity.User;

public class BudgetMapper {

    public static Budget toEntity(
            BudgetRequestDTO request,
            User user,
            ExpenseCategory category) {

        return Budget.builder()
                .user(user)
                .expenseCategory(category)
                .budgetAmount(request.getBudgetAmount())
                .month(request.getMonth())
                .year(request.getYear())
                .build();
    }

    public static BudgetResponseDTO toResponseDTO(Budget budget) {

        return BudgetResponseDTO.builder()
                .id(budget.getId())
                .budgetAmount(budget.getBudgetAmount())
                .month(budget.getMonth())
                .year(budget.getYear())
                .expenseCategory(budget.getExpenseCategory().getName())
                .createdAt(budget.getCreatedAt())
                .build();
    }
}