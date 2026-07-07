package com.manasa.smartexpenseplatform.mapper;

import com.manasa.smartexpenseplatform.dto.UserRequestDTO;
import com.manasa.smartexpenseplatform.dto.UserResponseDTO;
import com.manasa.smartexpenseplatform.entity.User;

public class UserMapper {

    public static User toEntity(UserRequestDTO request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        return user;
    }

    public static UserResponseDTO toResponseDTO(User user) {
        UserResponseDTO response = new UserResponseDTO();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        return response;
    }
}