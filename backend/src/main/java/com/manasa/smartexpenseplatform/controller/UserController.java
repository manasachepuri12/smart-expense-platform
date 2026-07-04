package com.manasa.smartexpenseplatform.controller;

import com.manasa.smartexpenseplatform.entity.User;
import com.manasa.smartexpenseplatform.service.UserService;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return userService.registerUser(user);
    }
}