package com.manasa.smartexpenseplatform.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.manasa.smartexpenseplatform.dto.LoginResponseDTO;
import com.manasa.smartexpenseplatform.dto.LoginRequestDTO;
import com.manasa.smartexpenseplatform.service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Tag(
    name = "Authentication",
    description = "User Registration and Login APIs"
)
public class AuthController {
    private final UserService userService;
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public LoginResponseDTO loginUser(
        @Valid @RequestBody LoginRequestDTO request) {
        return userService.loginUser(request);
    }
}