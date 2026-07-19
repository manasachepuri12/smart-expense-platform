package com.manasa.smartexpenseplatform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Response DTO containing expense target details")
public class ExpenseTargetResponseDTO {

    @Schema(
            description = "Unique identifier of the expense target",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Name of the expense target",
            example = "Self"
    )
    private String name;
}