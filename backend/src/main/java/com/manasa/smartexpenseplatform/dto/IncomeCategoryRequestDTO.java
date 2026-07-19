package com.manasa.smartexpenseplatform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request DTO for creating or updating an income category")
public class IncomeCategoryRequestDTO {

    @Schema(
            description = "Name of the income category",
            example = "Salary"
    )
    @NotBlank(message = "Category name is required")
    private String name;

}