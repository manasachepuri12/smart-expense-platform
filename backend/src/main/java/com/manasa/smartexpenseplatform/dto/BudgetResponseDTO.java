package com.manasa.smartexpenseplatform.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
public class BudgetResponseDTO {

    private Long id;

    private BigDecimal budgetAmount;

    private Integer month;

    private Integer year;

    private String expenseCategory;

    private LocalDateTime createdAt;
}