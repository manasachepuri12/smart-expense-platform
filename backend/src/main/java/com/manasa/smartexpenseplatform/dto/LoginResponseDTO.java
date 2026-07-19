package com.manasa.smartexpenseplatform.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response DTO returned after successful user login")
public class LoginResponseDTO {

    @Schema(
            description = "Status message indicating the login result",
            example = "Login successful"
    )
    private String message;

    @Schema(
            description = "JWT token used for authenticating future API requests",
            example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtYW5hc2FAZ21haWwuY29tIiwiaWF0IjoxNzE5ODAwMDAwLCJleHAiOjE3MTk4MDM2MDB9.signature"
    )
    private String token;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}