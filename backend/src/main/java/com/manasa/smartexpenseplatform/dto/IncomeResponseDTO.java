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
@Schema(description = "Response DTO containing income details")
public class IncomeResponseDTO {

    @Schema(
            description = "Unique identifier of the income",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Income amount",
            example = "50000.00"
    )
    private BigDecimal amount;

    @Schema(
            description = "Source of income",
            example = "Software Engineer Salary"
    )
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
    private LocalDate incomeDate;

    @Schema(
            description = "Name of the income category",
            example = "Salary"
    )
    private String incomeCategory;

    @Schema(
            description = "Date and time when the income record was created",
            example = "2026-07-12T09:30:45"
    )
    private LocalDateTime createdAt;
}