package com.manasa.smartexpenseplatform.dto;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseRequestDTO {
    @NotNull(message = "Expense category is required")
    private Long expenseCategoryId;

    @NotNull(message = "Expense target is required")
    private Long expenseTargetId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must not exceed 100 characters")
    private String title;

    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;

    @NotBlank(message = "Payment method is required")
    private String paymentMethod;

    @NotNull(message = "Expense date is required")
    private LocalDate expenseDate;
}
