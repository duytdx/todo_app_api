package com.example.todo_app.module.User.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.todo_app.module.User.model.User;
import com.example.todo_app.module.User.repository.UserRepositories;

@Service
public class UserService {
    private final UserRepositories userRepositories;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepositories userRepositories, PasswordEncoder passwordEncoder) {
        this.userRepositories = userRepositories;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        return userRepositories.findAll();
    }

    public User getUserById(Long id) {
        return userRepositories.findById(id).orElse(null);
    }

    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepositories.save(user);
    }

    public User updateUser(Long id, User userDetails) {
        User user = userRepositories.findById(id).orElse(null);
        if (user != null) {
            user.setUsername(userDetails.getUsername());
            user.setEmail(userDetails.getEmail());
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
            return userRepositories.save(user);
        }
        return null;
    }

    public void deleteUser(Long id) {
        userRepositories.deleteById(id);
    }
}
