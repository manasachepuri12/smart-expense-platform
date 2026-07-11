package com.manasa.smartexpenseplatform.service.impl;

import org.springframework.stereotype.Service;

import com.manasa.smartexpenseplatform.dto.ExpenseRequestDTO;
import com.manasa.smartexpenseplatform.dto.ExpenseResponseDTO;
import com.manasa.smartexpenseplatform.repository.ExpenseCategoryRepository;
import com.manasa.smartexpenseplatform.repository.ExpenseRepository;
import com.manasa.smartexpenseplatform.repository.ExpenseTargetRepository;
import com.manasa.smartexpenseplatform.repository.UserRepository;
import com.manasa.smartexpenseplatform.service.ExpenseService;

@Service
public class ExpenseServiceImpl implements ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final ExpenseCategoryRepository expenseCategoryRepository;
    private final ExpenseTargetRepository expenseTargetRepository;
    public ExpenseServiceImpl(
        ExpenseRepository expenseRepository,
        UserRepository userRepository,
        ExpenseCategoryRepository expenseCategoryRepository,
        ExpenseTargetRepository expenseTargetRepository) {

        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.expenseCategoryRepository = expenseCategoryRepository;
        this.expenseTargetRepository = expenseTargetRepository;
    }
    @Override
    public ExpenseResponseDTO createExpense(ExpenseRequestDTO request) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}