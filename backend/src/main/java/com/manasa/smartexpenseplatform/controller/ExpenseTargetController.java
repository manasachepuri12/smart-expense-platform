package com.manasa.smartexpenseplatform.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.manasa.smartexpenseplatform.dto.ExpenseTargetRequestDTO;
import com.manasa.smartexpenseplatform.dto.ExpenseTargetResponseDTO;
import com.manasa.smartexpenseplatform.service.ExpenseTargetService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/expense-targets")
@Tag(
        name = "Expense Targets",
        description = "APIs for managing expense targets"
)
public class ExpenseTargetController {

    private final ExpenseTargetService expenseTargetService;

    public ExpenseTargetController(
            ExpenseTargetService expenseTargetService) {

        this.expenseTargetService = expenseTargetService;
    }

    @Operation(
            summary = "Create a new expense target",
            description = "Creates a new expense target for the authenticated user."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Expense target created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping
    public ResponseEntity<ExpenseTargetResponseDTO> createTarget(
            @Valid @RequestBody ExpenseTargetRequestDTO request) {

        return new ResponseEntity<>(
                expenseTargetService.createTarget(request),
                HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get all expense targets",
            description = "Retrieves all expense targets of the authenticated user."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Expense targets retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping
    public ResponseEntity<List<ExpenseTargetResponseDTO>> getAllTargets() {

        return ResponseEntity.ok(
                expenseTargetService.getAllTargets());
    }

    @Operation(
            summary = "Get expense target by ID",
            description = "Retrieves a specific expense target using its ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Expense target retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Expense target not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ExpenseTargetResponseDTO> getTargetById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                expenseTargetService.getTargetById(id));
    }

    @Operation(
            summary = "Update expense target",
            description = "Updates an existing expense target using its ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Expense target updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Expense target not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ExpenseTargetResponseDTO> updateTarget(
            @PathVariable Long id,
            @Valid @RequestBody ExpenseTargetRequestDTO request) {

        return ResponseEntity.ok(
                expenseTargetService.updateTarget(id, request));
    }

    @Operation(
            summary = "Delete expense target",
            description = "Deletes an expense target using its ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Expense target deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Expense target not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTarget(
            @PathVariable Long id) {

        expenseTargetService.deleteTarget(id);

        return ResponseEntity.ok(
                "Expense Target deleted successfully.");
    }
}