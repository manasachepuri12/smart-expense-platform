package com.manasa.smartexpenseplatform.service;
import com.manasa.smartexpenseplatform.entity.User;
import java.util.List;
public interface UserService {
    User registerUser(User user);
    
    User loginUser(String email, String password);

    User getUserById(Long id);

    List<User> getAllUsers();

    User updateUser(Long id, User user);

    void deleteUser(Long id);

}