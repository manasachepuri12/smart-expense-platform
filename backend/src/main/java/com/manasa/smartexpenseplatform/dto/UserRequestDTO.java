package com.manasa.smartexpenseplatform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request DTO for user registration")
public class UserRequestDTO {

    @Schema(
            description = "Full name of the user",
            example = "Manasa"
    )
    @NotBlank(message = "Name is required")
    private String name;

    @Schema(
            description = "User's email address",
            example = "manasa@gmail.com"
    )
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @Schema(
            description = "User password (6-20 characters)",
            example = "Password@123"
    )
    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters")
    private String password;

}