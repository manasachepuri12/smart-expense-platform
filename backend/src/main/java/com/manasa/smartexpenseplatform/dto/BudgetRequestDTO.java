package com.manasa.smartexpenseplatform.dto;

import java.math.BigDecimal;

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
public class BudgetRequestDTO {

    @NotNull
    private BigDecimal budgetAmount;

    @NotNull
    private Integer month;

    @NotNull
    private Integer year;

    @NotNull
    private Long expenseCategoryId;
}