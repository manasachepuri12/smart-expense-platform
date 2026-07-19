package com.manasa.smartexpenseplatform.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseCategoryRequestDTO {

    @NotBlank(message = "Category name is required")
    private String name;

}