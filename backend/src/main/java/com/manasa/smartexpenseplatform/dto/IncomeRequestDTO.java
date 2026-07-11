package com.manasa.smartexpenseplatform.dto;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IncomeRequestDTO {
    private BigDecimal amount;

    private String sourceName;

    private String description;

    private LocalDate incomeDate;

    private Long incomeCategoryId;
}
