package com.manasa.smartexpenseplatform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Response DTO containing expense category details")
public class ExpenseCategoryResponseDTO {

    @Schema(
            description = "Unique identifier of the expense category",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Name of the expense category",
            example = "Food"
    )
    private String name;

}