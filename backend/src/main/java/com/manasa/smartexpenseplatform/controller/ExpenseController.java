package com.manasa.smartexpenseplatform.controller;

import com.manasa.smartexpenseplatform.dto.ExpenseRequestDTO;
import com.manasa.smartexpenseplatform.dto.ExpenseResponseDTO;
import com.manasa.smartexpenseplatform.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@Tag(
        name = "Expense Management",
        description = "APIs for managing expenses"
)
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @Operation(
            summary = "Create a new expense",
            description = "Creates a new expense for the authenticated user."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Expense created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping
    public ExpenseResponseDTO createExpense(
            @Valid @RequestBody ExpenseRequestDTO request) {

        return expenseService.createExpense(request);
    }

    @Operation(
            summary = "Get all expenses",
            description = "Retrieves all expenses of the authenticated user."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Expenses retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping
    public List<ExpenseResponseDTO> getAllExpenses() {
        return expenseService.getAllExpenses();
    }

    @Operation(
            summary = "Get expense by ID",
            description = "Retrieves a specific expense using its ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Expense retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Expense not found")
    })
    @GetMapping("/{id}")
    public ExpenseResponseDTO getExpenseById(@PathVariable Long id) {
        return expenseService.getExpenseById(id);
    }

    @Operation(
            summary = "Update an expense",
            description = "Updates an existing expense using its ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Expense updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Expense not found")
    })
    @PutMapping("/{id}")
    public ExpenseResponseDTO updateExpense(
            @PathVariable Long id,
            @Valid @RequestBody ExpenseRequestDTO request) {

        return expenseService.updateExpense(id, request);
    }

    @Operation(
            summary = "Delete an expense",
            description = "Deletes an expense using its ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Expense deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Expense not found")
    })
    @DeleteMapping("/{id}")
    public void deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
    }
}