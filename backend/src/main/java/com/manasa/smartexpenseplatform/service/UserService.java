package com.manasa.smartexpenseplatform.service;
import com.manasa.smartexpenseplatform.dto.UserRequestDTO;
import com.manasa.smartexpenseplatform.dto.UserResponseDTO;
import com.manasa.smartexpenseplatform.entity.User;
import java.util.List;
public interface UserService {
    UserResponseDTO registerUser(UserRequestDTO request);
    
    User loginUser(String email, String password);

    UserResponseDTO getUserById(Long id);

    public List<UserResponseDTO> getAllUsers();

    UserResponseDTO updateUser(Long id, UserRequestDTO request);

    void deleteUser(Long id);

}