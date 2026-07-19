package com.manasa.smartexpenseplatform.controller;

import com.manasa.smartexpenseplatform.service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;
import com.manasa.smartexpenseplatform.dto.UserRequestDTO;
import com.manasa.smartexpenseplatform.dto.UserResponseDTO;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
@RestController
@RequestMapping("/api/users")
@Tag(
    name = "User Management",
    description = "APIs for managing users"
)
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @Operation(
    summary = "Register a new user",
    description = "Creates a new user account after validating the request."
    )
    @ApiResponses({
    @ApiResponse(responseCode = "201", description = "User registered successfully"),
    @ApiResponse(responseCode = "400", description = "Invalid request data"),
    @ApiResponse(responseCode = "409", description = "Email already exists")
    })
    @PostMapping("/register")
    public UserResponseDTO registerUser(@Valid @RequestBody UserRequestDTO request) {
    System.out.println(">>> Register API called");
    return userService.registerUser(request);
    }

    @Operation(
    summary = "Get user by ID",
    description = "Retrieves the details of a user using the user ID."
    )
    @ApiResponses({
    @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
    @ApiResponse(responseCode = "404", description = "User not found"),
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/{id}")
    public UserResponseDTO getUserById(@PathVariable Long id) {
    return userService.getUserById(id);
    }

    @Operation(
    summary = "Get all users",
    description = "Retrieves a list of all registered users."
    )
    @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping
    public List<UserResponseDTO> getAllUsers() {
    return userService.getAllUsers();
    }

    @Operation(
    summary = "Update user",
    description = "Updates the details of an existing user."
    )
    @ApiResponses({
    @ApiResponse(responseCode = "200", description = "User updated successfully"),
    @ApiResponse(responseCode = "400", description = "Invalid request data"),
    @ApiResponse(responseCode = "404", description = "User not found"),
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PutMapping("/{id}")
    public UserResponseDTO updateUser(@PathVariable Long id,
                                  @Valid @RequestBody UserRequestDTO request) {
    return userService.updateUser(id, request);
    }

    @Operation(
    summary = "Delete user",
    description = "Deletes a user using the user ID."
    )
    @ApiResponses({
    @ApiResponse(responseCode = "200", description = "User deleted successfully"),
    @ApiResponse(responseCode = "404", description = "User not found"),
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
    userService.deleteUser(id);
    }
}