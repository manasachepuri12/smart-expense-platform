package com.manasa.smartexpenseplatform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Response DTO containing income category details")
public class IncomeCategoryResponseDTO {

    @Schema(
            description = "Unique identifier of the income category",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Name of the income category",
            example = "Salary"
    )
    private String name;
}