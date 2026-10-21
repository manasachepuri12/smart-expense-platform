package com.manasa.smartexpenseplatform.service;

import com.manasa.smartexpenseplatform.dto.LoginRequestDTO;
import com.manasa.smartexpenseplatform.dto.LoginResponseDTO;
import com.manasa.smartexpenseplatform.dto.UserRequestDTO;
import com.manasa.smartexpenseplatform.dto.UserResponseDTO;
import java.util.List;
import com.manasa.smartexpenseplatform.dto.ProfileResponseDTO;
import com.manasa.smartexpenseplatform.dto.ProfileUpdateRequestDTO;
import com.manasa.smartexpenseplatform.dto.ChangePasswordRequestDTO;
public interface UserService {
    UserResponseDTO registerUser(UserRequestDTO request);

    LoginResponseDTO loginUser(LoginRequestDTO request);

    UserResponseDTO getUserById(Long id);

    public List<UserResponseDTO> getAllUsers();

    UserResponseDTO updateUser(Long id, UserRequestDTO request);

    void deleteUser(Long id);

    ProfileResponseDTO getMyProfile(String authenticatedEmail);

    ProfileResponseDTO updateMyProfile(
            String authenticatedEmail,
            ProfileUpdateRequestDTO request);
            void changeMyPassword(
        String authenticatedEmail,
        ChangePasswordRequestDTO request
);
}