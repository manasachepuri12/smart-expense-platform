package com.manasa.smartexpenseplatform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request DTO for creating or updating an income")
public class IncomeRequestDTO {

    @Schema(
            description = "Income amount",
            example = "50000.00"
    )
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    @Schema(
            description = "Source of income",
            example = "Software Engineer Salary"
    )
    @NotBlank(message = "Source name is required")
    private String sourceName;

    @Schema(
            description = "Additional details about the income",
            example = "July 2026 salary credited"
    )
    private String description;

    @Schema(
            description = "Date the income was received",
            example = "2026-07-12"
    )
    @NotNull(message = "Income date is required")
    private LocalDate incomeDate;

    @Schema(
            description = "ID of the income category",
            example = "1"
    )
    @NotNull(message = "Income category is required")
    private Long incomeCategoryId;
}