package com.manasa.smartexpenseplatform.mapper;

import com.manasa.smartexpenseplatform.dto.ExpenseRequestDTO;
import com.manasa.smartexpenseplatform.dto.ExpenseResponseDTO;
import com.manasa.smartexpenseplatform.entity.Expense;
import com.manasa.smartexpenseplatform.entity.ExpenseCategory;
import com.manasa.smartexpenseplatform.entity.ExpenseTarget;
import com.manasa.smartexpenseplatform.entity.User;

public class ExpenseMapper {
    public static Expense toEntity(
        ExpenseRequestDTO request,
        User user,
        ExpenseCategory category,
        ExpenseTarget target) {

        return Expense.builder()
        .user(user)
        .expenseCategory(category)
        .expenseTarget(target)
        .amount(request.getAmount())
        .title(request.getTitle())
        .description(request.getDescription())
        .paymentMethod(request.getPaymentMethod())
        .expenseDate(request.getExpenseDate())
        .build();
    }

    public static ExpenseResponseDTO toResponseDTO(Expense expense) {

        return ExpenseResponseDTO.builder()
        .id(expense.getId())
        .amount(expense.getAmount())
        .title(expense.getTitle())
        .description(expense.getDescription())
        .paymentMethod(expense.getPaymentMethod())
        .expenseDate(expense.getExpenseDate())
        .createdAt(expense.getCreatedAt())
        .expenseCategory(expense.getExpenseCategory().getName())
        .expenseTarget(expense.getExpenseTarget().getName())
        .build();
    }
}
