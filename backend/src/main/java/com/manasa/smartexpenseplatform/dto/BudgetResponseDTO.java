package com.manasa.smartexpenseplatform.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Response DTO containing budget details")
public class BudgetResponseDTO {

    @Schema(
            description = "Unique identifier of the budget",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Allocated budget amount",
            example = "10000.00"
    )
    private BigDecimal budgetAmount;

    @Schema(
            description = "Budget month",
            example = "7"
    )
    private Integer month;

    @Schema(
            description = "Budget year",
            example = "2026"
    )
    private Integer year;

    @Schema(
            description = "Name of the expense category associated with the budget",
            example = "Food"
    )
    private String expenseCategory;

    @Schema(
            description = "Date and time when the budget was created",
            example = "2026-07-12T11:30:45"
    )
    private LocalDateTime createdAt;
}