package com.manasa.smartexpenseplatform.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.manasa.smartexpenseplatform.dto.ExpenseCategoryRequestDTO;
import com.manasa.smartexpenseplatform.dto.ExpenseCategoryResponseDTO;
import com.manasa.smartexpenseplatform.service.ExpenseCategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/expense-categories")
@Validated
@Tag(
        name = "Expense Categories",
        description = "APIs for managing expense categories"
)
public class ExpenseCategoryController {

    private final ExpenseCategoryService expenseCategoryService;

    public ExpenseCategoryController(ExpenseCategoryService expenseCategoryService) {
        this.expenseCategoryService = expenseCategoryService;
    }

    @Operation(
            summary = "Create a new expense category",
            description = "Creates a new expense category for the authenticated user."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Expense category created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping
    public ResponseEntity<ExpenseCategoryResponseDTO> createCategory(
            @Valid @RequestBody ExpenseCategoryRequestDTO request) {

        ExpenseCategoryResponseDTO response =
                expenseCategoryService.createCategory(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get all expense categories",
            description = "Retrieves all expense categories of the authenticated user."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Expense categories retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping
    public ResponseEntity<List<ExpenseCategoryResponseDTO>> getAllCategories() {

        return ResponseEntity.ok(
                expenseCategoryService.getAllCategories());
    }

    @Operation(
            summary = "Get expense category by ID",
            description = "Retrieves a specific expense category using its ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Expense category retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Expense category not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ExpenseCategoryResponseDTO> getCategoryById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                expenseCategoryService.getCategoryById(id));
    }

    @Operation(
            summary = "Update expense category",
            description = "Updates an existing expense category using its ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Expense category updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Expense category not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ExpenseCategoryResponseDTO> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody ExpenseCategoryRequestDTO request) {

        return ResponseEntity.ok(
                expenseCategoryService.updateCategory(id, request));
    }

    @Operation(
            summary = "Delete expense category",
            description = "Deletes an expense category using its ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Expense category deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Expense category not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(
            @PathVariable Long id) {

        expenseCategoryService.deleteCategory(id);

        return ResponseEntity.ok("Expense Category deleted successfully.");
    }
}