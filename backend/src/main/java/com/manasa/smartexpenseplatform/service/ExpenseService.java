package com.manasa.smartexpenseplatform.service;

import com.manasa.smartexpenseplatform.dto.ExpenseRequestDTO;
import com.manasa.smartexpenseplatform.dto.ExpenseResponseDTO;

public interface ExpenseService {

    ExpenseResponseDTO createExpense(ExpenseRequestDTO request);

}