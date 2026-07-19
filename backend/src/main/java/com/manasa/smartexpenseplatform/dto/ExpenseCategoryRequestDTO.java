package com.manasa.smartexpenseplatform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request DTO for creating or updating an expense category")
public class ExpenseCategoryRequestDTO {

    @Schema(
            description = "Name of the expense category",
            example = "Food"
    )
    @NotBlank(message = "Category name is required")
    private String name;

}