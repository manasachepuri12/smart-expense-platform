package com.manasa.smartexpenseplatform.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseResponseDTO {
    private Long id;
    private BigDecimal amount;
    private String title;
    private String description;
    private String expenseCategory;
    private String expenseTarget;
    private String paymentMethod;
    private LocalDate expenseDate;
    private LocalDateTime createdAt;
}