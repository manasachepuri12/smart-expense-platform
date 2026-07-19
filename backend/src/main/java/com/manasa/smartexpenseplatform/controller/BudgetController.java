package com.manasa.smartexpenseplatform.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.manasa.smartexpenseplatform.dto.BudgetRequestDTO;
import com.manasa.smartexpenseplatform.dto.BudgetResponseDTO;
import com.manasa.smartexpenseplatform.service.BudgetService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/budgets")
@Tag(
    name = "Budget Management",
    description = "APIs for managing budgets"
)
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @PostMapping
    public BudgetResponseDTO createBudget(
            @Valid @RequestBody BudgetRequestDTO request) {

        return budgetService.createBudget(request);
    }

    @GetMapping
    public List<BudgetResponseDTO> getAllBudgets() {
        return budgetService.getAllBudgets();
    }

    @GetMapping("/{id}")
    public BudgetResponseDTO getBudgetById(@PathVariable Long id) {
        return budgetService.getBudgetById(id);
    }

    @PutMapping("/{id}")
    public BudgetResponseDTO updateBudget(
            @PathVariable Long id,
            @Valid @RequestBody BudgetRequestDTO request) {

        return budgetService.updateBudget(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteBudget(@PathVariable Long id) {
        budgetService.deleteBudget(id);
    }
}