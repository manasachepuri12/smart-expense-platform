package com.manasa.smartexpenseplatform.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.manasa.smartexpenseplatform.dto.BudgetRequestDTO;
import com.manasa.smartexpenseplatform.dto.BudgetResponseDTO;
import com.manasa.smartexpenseplatform.service.BudgetService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(
            summary = "Create a new budget",
            description = "Creates a new budget for the authenticated user."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Budget created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping
    public BudgetResponseDTO createBudget(
            @Valid @RequestBody BudgetRequestDTO request) {

        return budgetService.createBudget(request);
    }

    @Operation(
            summary = "Get all budgets",
            description = "Retrieves all budgets of the authenticated user."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Budgets retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping
    public List<BudgetResponseDTO> getAllBudgets() {
        return budgetService.getAllBudgets();
    }

    @Operation(
            summary = "Get budget by ID",
            description = "Retrieves a specific budget using its ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Budget retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Budget not found")
    })
    @GetMapping("/{id}")
    public BudgetResponseDTO getBudgetById(@PathVariable Long id) {
        return budgetService.getBudgetById(id);
    }

    @Operation(
            summary = "Update budget",
            description = "Updates an existing budget using its ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Budget updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Budget not found")
    })
    @PutMapping("/{id}")
    public BudgetResponseDTO updateBudget(
            @PathVariable Long id,
            @Valid @RequestBody BudgetRequestDTO request) {

        return budgetService.updateBudget(id, request);
    }

    @Operation(
            summary = "Delete budget",
            description = "Deletes a budget using its ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Budget deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Budget not found")
    })
    @DeleteMapping("/{id}")
    public void deleteBudget(@PathVariable Long id) {
        budgetService.deleteBudget(id);
    }
}