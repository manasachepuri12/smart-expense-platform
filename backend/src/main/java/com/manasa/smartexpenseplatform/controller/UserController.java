package com.manasa.smartexpenseplatform.controller;

import com.manasa.smartexpenseplatform.entity.User;
import com.manasa.smartexpenseplatform.service.UserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;
import com.manasa.smartexpenseplatform.dto.UserRequestDTO;
import com.manasa.smartexpenseplatform.dto.UserResponseDTO;
import java.util.List;
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @PostMapping("/register")
    public UserResponseDTO registerUser(@Valid @RequestBody UserRequestDTO request) {
        return userService.registerUser(request);
    }

    @GetMapping("/{id}")
    public UserResponseDTO getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping
    public List<UserResponseDTO> getAllUsers() {
     return userService.getAllUsers();
    }

    @PutMapping("/{id}")
    public UserResponseDTO updateUser(@PathVariable Long id,
                                  @Valid @RequestBody UserRequestDTO request) {
        return userService.updateUser(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}