package com.manasa.smartexpenseplatform.service.impl;

import com.manasa.smartexpenseplatform.entity.User;
import com.manasa.smartexpenseplatform.mapper.UserMapper;
import com.manasa.smartexpenseplatform.repository.UserRepository;
import com.manasa.smartexpenseplatform.security.JwtService;
import com.manasa.smartexpenseplatform.service.UserService;
import com.manasa.smartexpenseplatform.dto.LoginRequestDTO;
import com.manasa.smartexpenseplatform.dto.LoginResponseDTO;
import com.manasa.smartexpenseplatform.dto.UserRequestDTO;
import com.manasa.smartexpenseplatform.dto.UserResponseDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.manasa.smartexpenseplatform.dto.ProfileResponseDTO;
import com.manasa.smartexpenseplatform.dto.ProfileUpdateRequestDTO;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.ArrayList;
import java.util.List;
import com.manasa.smartexpenseplatform.exception.EmailAlreadyExistsException;
import com.manasa.smartexpenseplatform.dto.ChangePasswordRequestDTO;
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserServiceImpl(UserRepository userRepository,
            PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public UserResponseDTO registerUser(UserRequestDTO request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
        User user = UserMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        return UserMapper.toResponseDTO(savedUser);
    }

    @Override
    public LoginResponseDTO loginUser(LoginRequestDTO request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }
        String token = jwtService.generateToken(user.getEmail());
        LoginResponseDTO response = new LoginResponseDTO();
        response.setMessage("Login successful");
        response.setToken(token);
        return response;
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
        user.setPassword(passwordEncoder.encode(request.getPassword()));
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

    @Override
    public ProfileResponseDTO getMyProfile(String authenticatedEmail) {
        User user = userRepository.findByEmail(authenticatedEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ProfileResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    @Override
    public ProfileResponseDTO updateMyProfile(
            String authenticatedEmail,
            ProfileUpdateRequestDTO request) {

        User user = userRepository.findByEmail(authenticatedEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String newEmail = request.getEmail().trim();

        if (!user.getEmail().equalsIgnoreCase(newEmail)
                && userRepository.existsByEmail(newEmail)) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        user.setName(request.getName().trim());
        user.setEmail(newEmail);

        User updatedUser = userRepository.save(user);

        return ProfileResponseDTO.builder()
                .id(updatedUser.getId())
                .name(updatedUser.getName())
                .email(updatedUser.getEmail())
                .build();
    }

@Override
public void changeMyPassword(
        String authenticatedEmail,
        ChangePasswordRequestDTO request) {

    User user = userRepository.findByEmail(authenticatedEmail)
            .orElseThrow(() -> new RuntimeException("User not found"));

    if (!passwordEncoder.matches(
            request.getCurrentPassword(), user.getPassword())) {
        throw new IllegalArgumentException("Current password is incorrect");
    }

    if (!request.getNewPassword().equals(request.getConfirmPassword())) {
        throw new IllegalArgumentException(
                "New password and confirmation do not match");
    }

    if (passwordEncoder.matches(
            request.getNewPassword(), user.getPassword())) {
        throw new IllegalArgumentException(
                "New password must be different from current password");
    }

    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
    userRepository.save(user);
}

}
