package com.manasa.smartexpenseplatform.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseTargetRequestDTO {

    @NotBlank(message = "Target name is required")
    private String name;
}