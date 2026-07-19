package com.manasa.smartexpenseplatform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request DTO for creating or updating an expense")
public class ExpenseRequestDTO {

    @Schema(
            description = "ID of the expense category",
            example = "1"
    )
    @NotNull(message = "Expense category is required")
    private Long expenseCategoryId;

    @Schema(
            description = "ID of the expense target",
            example = "2"
    )
    @NotNull(message = "Expense target is required")
    private Long expenseTargetId;

    @Schema(
            description = "Amount spent",
            example = "2500.50"
    )
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    @Schema(
            description = "Title of the expense",
            example = "Monthly Grocery Shopping"
    )
    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must not exceed 100 characters")
    private String title;

    @Schema(
            description = "Additional details about the expense",
            example = "Purchased groceries from D-Mart"
    )
    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;

    @Schema(
            description = "Payment method used for the expense",
            example = "UPI"
    )
    @NotBlank(message = "Payment method is required")
    private String paymentMethod;

    @Schema(
            description = "Date on which the expense occurred",
            example = "2026-07-12"
    )
    @NotNull(message = "Expense date is required")
    private LocalDate expenseDate;
}