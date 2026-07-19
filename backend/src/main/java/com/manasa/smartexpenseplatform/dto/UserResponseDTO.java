package com.manasa.smartexpenseplatform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Response DTO containing user details")
public class UserResponseDTO {

    @Schema(
            description = "Unique identifier of the user",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Full name of the user",
            example = "Manasa"
    )
    private String name;

    @Schema(
            description = "User's email address",
            example = "manasa@gmail.com"
    )
    private String email;
}