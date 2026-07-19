package com.manasa.smartexpenseplatform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request DTO for creating or updating an expense target")
public class ExpenseTargetRequestDTO {

    @Schema(
            description = "Name of the expense target",
            example = "Self"
    )
    @NotBlank(message = "Target name is required")
    private String name;

}