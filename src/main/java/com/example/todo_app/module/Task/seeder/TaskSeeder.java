package com.example.todo_app.module.Task.seeder;

import com.example.todo_app.module.Task.model.Task;
import com.example.todo_app.module.Task.repository.TaskRepositories;
import com.example.todo_app.module.User.model.User;
import com.example.todo_app.module.User.repository.UserRepositories;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Data seeder để tạo dữ liệu mẫu cho Task
 * Sẽ chạy tự động khi application khởi động
 */
@Component
@Order(2)  // Chạy sau UserSeeder
public class TaskSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(TaskSeeder.class);

    private final TaskRepositories taskRepository;
    private final UserRepositories userRepository;

    @Value("${app.seeding.enabled:false}")
    private boolean seedingEnabled;

    public TaskSeeder(TaskRepositories taskRepository, UserRepositories userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (!seedingEnabled) {
            logger.info("Data seeding is disabled.");
            return;
        }

        // Chỉ seed nếu database trống
        if (taskRepository.count() == 0) {
            logger.info("Starting Task data seeding...");
            seedTasks();
            logger.info("Task data seeding completed!");
        } else {
            logger.info("Tasks already exist. Skipping seeding.");
        }
    }

    private void seedTasks() {
        // Lấy user đầu tiên để assign cho tasks
        User defaultUser = userRepository.findAll().stream()
            .findFirst()
            .orElseThrow(() -> new RuntimeException("No users found. Please run UserSeeder first."));

        logger.info("Assigning tasks to user: {}", defaultUser.getUsername());

        List<Task> tasks = Arrays.asList(
            createTask("Complete project documentation", "Write comprehensive documentation for the todo app project", false, defaultUser),
            createTask("Fix authentication bugs", "Resolve issues with user login and token validation", false, defaultUser),
            createTask("Review pull requests", "Check and merge pending pull requests from team members", false, defaultUser),
            createTask("Update dependencies", "Upgrade Spring Boot and other libraries to latest stable versions", false, defaultUser),
            createTask("Write unit tests", "Add test coverage for user and task services", false, defaultUser),
            createTask("Setup CI/CD pipeline", "Configure GitHub Actions for automated testing and deployment", true, defaultUser),
            createTask("Database optimization", "Add indexes and optimize slow queries", false, defaultUser),
            createTask("Implement email notifications", "Send email alerts for task deadlines and updates", false, defaultUser),
            createTask("Create API documentation", "Generate Swagger/OpenAPI documentation for REST endpoints", true, defaultUser),
            createTask("Security audit", "Review and fix security vulnerabilities in the application", false, defaultUser),
            createTask("Refactor legacy code", "Clean up and modernize old codebase sections", false, defaultUser),
            createTask("Design new landing page", "Create mockups and implement new homepage design", true, defaultUser),
            createTask("Performance testing", "Load test the application and identify bottlenecks", false, defaultUser),
            createTask("Mobile responsiveness", "Ensure UI works properly on mobile devices", true, defaultUser),
            createTask("Backup strategy", "Implement automated database backup solution", false, defaultUser)
        );

        taskRepository.saveAll(tasks);
        logger.info("Seeded {} tasks", tasks.size());
    }

    private Task createTask(String title, String description, boolean completed, User user) {
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setCompleted(completed);
        task.setCreateBy(user);
        logger.debug("Created task '{}'", title);
        return task;
    }
}
