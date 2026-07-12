package com.manasa.smartexpenseplatform.service;

import java.util.List;

import com.manasa.smartexpenseplatform.dto.BudgetRequestDTO;
import com.manasa.smartexpenseplatform.dto.BudgetResponseDTO;

public interface BudgetService {

    BudgetResponseDTO createBudget(BudgetRequestDTO request);

    List<BudgetResponseDTO> getAllBudgets();

    BudgetResponseDTO getBudgetById(Long id);

    BudgetResponseDTO updateBudget(Long id, BudgetRequestDTO request);

    void deleteBudget(Long id);
}