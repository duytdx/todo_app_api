package com.example.todo_app.module.Task.seeder;

import com.example.todo_app.module.Task.model.Task;
import com.example.todo_app.module.Task.repository.TaskRepositories;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Data seeder để tạo dữ liệu mẫu cho Task
 * Sẽ chạy tự động khi application khởi động
 */
@Component
public class TaskSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(TaskSeeder.class);

    private final TaskRepositories taskRepository;

    @Value("${app.seeding.enabled:false}")
    private boolean seedingEnabled;

    public TaskSeeder(TaskRepositories taskRepository) {
        this.taskRepository = taskRepository;
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
        List<Task> tasks = Arrays.asList(
            createTask("Complete project documentation", "Write comprehensive documentation for the todo app project", false),
            createTask("Fix authentication bugs", "Resolve issues with user login and token validation", false),
            createTask("Review pull requests", "Check and merge pending pull requests from team members", false),
            createTask("Update dependencies", "Upgrade Spring Boot and other libraries to latest stable versions", false),
            createTask("Write unit tests", "Add test coverage for user and task services", false),
            createTask("Setup CI/CD pipeline", "Configure GitHub Actions for automated testing and deployment", true),
            createTask("Database optimization", "Add indexes and optimize slow queries", false),
            createTask("Implement email notifications", "Send email alerts for task deadlines and updates", false),
            createTask("Create API documentation", "Generate Swagger/OpenAPI documentation for REST endpoints", true),
            createTask("Security audit", "Review and fix security vulnerabilities in the application", false),
            createTask("Refactor legacy code", "Clean up and modernize old codebase sections", false),
            createTask("Design new landing page", "Create mockups and implement new homepage design", true),
            createTask("Performance testing", "Load test the application and identify bottlenecks", false),
            createTask("Mobile responsiveness", "Ensure UI works properly on mobile devices", true),
            createTask("Backup strategy", "Implement automated database backup solution", false)
        );

        taskRepository.saveAll(tasks);
        logger.info("Seeded {} tasks", tasks.size());
    }

    private Task createTask(String title, String description, boolean completed) {
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setCompleted(completed);
        logger.debug("Created task '{}'", title);
        return task;
    }
}
