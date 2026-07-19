package com.manasa.smartexpenseplatform.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request DTO for creating or updating a budget")
public class BudgetRequestDTO {

    @Schema(
            description = "Budget amount allocated",
            example = "10000.00"
    )
    @NotNull(message = "Budget amount is required")
    @DecimalMin(value = "0.01", message = "Budget amount must be greater than 0")
    private BigDecimal budgetAmount;

    @Schema(
            description = "Budget month (1-12)",
            example = "7"
    )
    @NotNull(message = "Month is required")
    private Integer month;

    @Schema(
            description = "Budget year",
            example = "2026"
    )
    @NotNull(message = "Year is required")
    private Integer year;

    @Schema(
            description = "ID of the expense category associated with the budget",
            example = "1"
    )
    @NotNull(message = "Expense category is required")
    private Long expenseCategoryId;
}