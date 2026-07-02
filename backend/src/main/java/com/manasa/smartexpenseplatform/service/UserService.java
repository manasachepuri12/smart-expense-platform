package com.manasa.smartexpenseplatform.service;
import com.manasa.smartexpenseplatform.entity.User;
public interface UserService {
    User registerUser(User user);
    
    User loginUser(String email, String password);

    User getUserById(Long id);

    User updateUser(Long id, User user);

    void deleteUser(Long id);
}