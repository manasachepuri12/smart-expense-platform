package com.manasa.smartexpenseplatform.controller;

import com.manasa.smartexpenseplatform.dto.IncomeRequestDTO;
import com.manasa.smartexpenseplatform.dto.IncomeResponseDTO;
import com.manasa.smartexpenseplatform.service.IncomeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/income")
@Tag(
        name = "Income Management",
        description = "APIs for managing incomes"
)
public class IncomeController {

    private final IncomeService incomeService;

    public IncomeController(IncomeService incomeService) {
        this.incomeService = incomeService;
    }

    @Operation(
            summary = "Create a new income",
            description = "Creates a new income record for the authenticated user."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Income created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping
    public IncomeResponseDTO createIncome(
            @Valid @RequestBody IncomeRequestDTO request) {

        return incomeService.createIncome(request);
    }

    @Operation(
            summary = "Get all income",
            description = "Retrieves all income records of the authenticated user."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Income retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping
    public List<IncomeResponseDTO> getAllIncome() {
        return incomeService.getAllIncome();
    }

    @Operation(
            summary = "Get income by ID",
            description = "Retrieves a specific income record using its ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Income retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Income not found")
    })
    @GetMapping("/{id}")
    public IncomeResponseDTO getIncomeById(@PathVariable Long id) {
        return incomeService.getIncomeById(id);
    }

    @Operation(
            summary = "Update income",
            description = "Updates an existing income record using its ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Income updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Income not found")
    })
    @PutMapping("/{id}")
    public IncomeResponseDTO updateIncome(
            @PathVariable Long id,
            @Valid @RequestBody IncomeRequestDTO request) {

        return incomeService.updateIncome(id, request);
    }

    @Operation(
            summary = "Delete income",
            description = "Deletes an income record using its ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Income deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Income not found")
    })
    @DeleteMapping("/{id}")
    public void deleteIncome(@PathVariable Long id) {
        incomeService.deleteIncome(id);
    }
}