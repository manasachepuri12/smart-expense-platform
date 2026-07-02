package com.manasa.smartexpenseplatform.service.impl;

import com.manasa.smartexpenseplatform.entity.User;
import com.manasa.smartexpenseplatform.repository.UserRepository;
import com.manasa.smartexpenseplatform.service.UserService;
import org.springframework.stereotype.Service;
import java.util.Optional;
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
   @Override
    public User registerUser(User user) {
        return userRepository.save(user);
    }
    @Override
    public User loginUser(String email, String password) {
     
        throw new UnsupportedOperationException("Unimplemented method 'loginUser'");
    }
    @Override
    public User getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
        return user.get();
        }
        throw new RuntimeException("User not found");
    }
    @Override
    public User updateUser(Long id, User user) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            existingUser.get().setName(user.getName());
            existingUser.get().setEmail(user.getEmail());
            existingUser.get().setPassword(user.getPassword());
            return userRepository.save(existingUser.get());
        }
        throw new RuntimeException("User not found");
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
