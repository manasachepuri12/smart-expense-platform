package com.manasa.smartexpenseplatform.service.impl;

import com.manasa.smartexpenseplatform.entity.User;
import com.manasa.smartexpenseplatform.mapper.UserMapper;
import com.manasa.smartexpenseplatform.repository.UserRepository;
import com.manasa.smartexpenseplatform.service.UserService;
import com.manasa.smartexpenseplatform.dto.UserRequestDTO;
import com.manasa.smartexpenseplatform.dto.UserResponseDTO;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.ArrayList;
import java.util.List;
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
   @Override
    public UserResponseDTO registerUser(UserRequestDTO request) {
        User user = UserMapper.toEntity(request);
        User savedUser = userRepository.save(user);
        return UserMapper.toResponseDTO(savedUser);
    }
    @Override
    public User loginUser(String email, String password) {
     
        throw new UnsupportedOperationException("Unimplemented method 'loginUser'");
    }
    @Override
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return UserMapper.toResponseDTO(user);
    }
    @Override
    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserResponseDTO> responses = new ArrayList<>();
        for (User user : users) {
            responses.add(UserMapper.toResponseDTO(user));
        }
        return responses;
    }
    @Override
    public UserResponseDTO updateUser(Long id, UserRequestDTO request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        User updatedUser = userRepository.save(user);
        return UserMapper.toResponseDTO(updatedUser);
    }
    @Override
    public void deleteUser(Long id) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            userRepository.delete(existingUser.get());
            return;
        }
        throw new RuntimeException("User not found");
    }
}
