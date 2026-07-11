package com.manasa.smartexpenseplatform.service;

import com.manasa.smartexpenseplatform.dto.ExpenseRequestDTO;
import com.manasa.smartexpenseplatform.dto.ExpenseResponseDTO;
import java.util.List;
public interface ExpenseService {

    ExpenseResponseDTO createExpense(ExpenseRequestDTO request);
    List<ExpenseResponseDTO> getAllExpenses();
    ExpenseResponseDTO getExpenseById(Long id);
    ExpenseResponseDTO updateExpense(Long id, ExpenseRequestDTO request);
    void deleteExpense(Long id);
}