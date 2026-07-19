package com.manasa.smartexpenseplatform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Response DTO containing expense details")
public class ExpenseResponseDTO {

    @Schema(
            description = "Unique identifier of the expense",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Amount spent",
            example = "2500.50"
    )
    private BigDecimal amount;

    @Schema(
            description = "Title of the expense",
            example = "Monthly Grocery Shopping"
    )
    private String title;

    @Schema(
            description = "Additional details about the expense",
            example = "Purchased groceries from D-Mart"
    )
    private String description;

    @Schema(
            description = "Name of the expense category",
            example = "Food"
    )
    private String expenseCategory;

    @Schema(
            description = "Name of the expense target",
            example = "Self"
    )
    private String expenseTarget;

    @Schema(
            description = "Payment method used",
            example = "UPI"
    )
    private String paymentMethod;

    @Schema(
            description = "Date when the expense occurred",
            example = "2026-07-12"
    )
    private LocalDate expenseDate;

    @Schema(
            description = "Date and time when the expense was created",
            example = "2026-07-12T10:30:45"
    )
    private LocalDateTime createdAt;
}