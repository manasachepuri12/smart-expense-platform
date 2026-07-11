package com.manasa.smartexpenseplatform.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IncomeResponseDTO {
    private Long id;

    private BigDecimal amount;

    private String sourceName;

    private String description;

    private LocalDate incomeDate;

    private String incomeCategory;

    private LocalDateTime createdAt;
}
