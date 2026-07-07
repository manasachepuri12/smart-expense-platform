package com.manasa.smartexpenseplatform.dto;
import lombok.*;
import jakarta.validation.constraints.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestDTO {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters")
    private String password;

}
