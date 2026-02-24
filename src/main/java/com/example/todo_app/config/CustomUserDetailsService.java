package com.example.todo_app.config;

import java.util.ArrayList;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.example.todo_app.module.User.model.User;
import com.example.todo_app.module.User.repository.UserRepositories;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepositories userRepositories;

    public CustomUserDetailsService(UserRepositories userRepositories) {
        this.userRepositories = userRepositories;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameorEmail) throws UsernameNotFoundException {
        User user = userRepositories.findByUsername(usernameorEmail)
                .orElseGet(() -> userRepositories.findByEmail(usernameorEmail)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found with username or email: " + usernameorEmail)));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                new ArrayList<>()
        );
    }

}
