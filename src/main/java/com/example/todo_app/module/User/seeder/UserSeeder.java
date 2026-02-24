package com.example.todo_app.module.User.seeder;

import com.example.todo_app.module.User.model.User;
import com.example.todo_app.module.User.repository.UserRepositories;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Data seeder để tạo dữ liệu mẫu cho User
 * Sẽ chạy tự động khi application khởi động
 * Password được hash bằng BCrypt để sẵn sàng tích hợp JWT
 */
@Component
public class UserSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(UserSeeder.class);

    private final UserRepositories userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.seeding.enabled:false}")
    private boolean seedingEnabled;

    public UserSeeder(UserRepositories userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (!seedingEnabled) {
            logger.info("Data seeding is disabled.");
            return;
        }

        // Chỉ seed nếu database trống
        if (userRepository.count() == 0) {
            logger.info("Starting User data seeding...");
            seedUsers();
            logger.info("User data seeding completed!");
        } else {
            logger.info("Users already exist. Skipping seeding.");
        }
    }

    private void seedUsers() {
        List<User> users = Arrays.asList(
            createUser(1L, "admin", "admin@example.com", "12345"),
            createUser(2L, "john_doe", "john.doe@example.com", "john123"),
            createUser(3L, "jane_smith", "jane.smith@example.com", "jane123"),
            createUser(4L, "bob_wilson", "bob.wilson@example.com", "bob123"),
            createUser(5L, "alice_jones", "alice.jones@example.com", "alice123"),
            createUser(6L, "charlie_brown", "charlie.brown@example.com", "charlie123"),
            createUser(7L, "diana_prince", "diana.prince@example.com", "diana123"),
            createUser(8L, "evan_peters", "evan.peters@example.com", "evan123"),
            createUser(9L, "fiona_gallagher", "fiona.gallagher@example.com", "fiona123"),
            createUser(10L, "george_miller", "george.miller@example.com", "george123")
        );

        userRepository.saveAll(users);
        logger.info("Seeded {} users", users.size());
    }

    private User createUser(Long id, String username, String email, String rawPassword) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        logger.debug("Created user '{}' with hashed password", username);
        return user;
    }
}
