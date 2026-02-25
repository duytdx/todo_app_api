package com.example.todo_app.module.User.service;

import java.util.List;

import org.hibernate.sql.Update;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.todo_app.module.User.dto.UpdateUserRequest;
import com.example.todo_app.module.User.dto.CreateUserRequest;
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

    public Page<User> getAllUsers(Pageable pageable) {
        try {
            return userRepositories.findAll(pageable);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving users from database", e);
        }
    }

    public User getUserById(Long id) {
        try {
            return userRepositories.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving user from database", e);
        }
    }

    public User createUser(CreateUserRequest request) {
        try {
            if(userRepositories.existsByUsername(request.username()) || userRepositories.existsByEmail(request.email())) {
                throw new IllegalArgumentException("Username or email already exists");
            }
            User user = new User();
            user.setUsername(request.username());
            user.setEmail(request.email());
            user.setPassword(passwordEncoder.encode(request.password()));
            user.setRole(request.role());
            return userRepositories.save(user);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error creating user: " + e.getMessage(), e);
        }
    }

    public User updateUser(Long id, UpdateUserRequest userDetails) {
        try {
            User user = userRepositories.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

            user.setUsername(userDetails.username());
            user.setEmail(userDetails.email());
            user.setPassword(passwordEncoder.encode(userDetails.password()));
            user.setRole(userDetails.role());
            return userRepositories.save(user);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error updating user: " + e.getMessage(), e);
        }
    }

    public void deleteUser(Long id) {
        try {
            if (!userRepositories.existsById(id)) {
                throw new IllegalArgumentException("User not found with id: " + id);
            }
            userRepositories.deleteById(id);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error deleting user: " + e.getMessage(), e);
        }
    }
}
