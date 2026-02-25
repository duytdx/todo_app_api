package com.example.todo_app.module.Task.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.todo_app.module.Task.dto.CreateTaskRequest;
import com.example.todo_app.module.Task.dto.UpdateTaskRequest;
import com.example.todo_app.module.Task.service.TaskService;
import com.example.todo_app.module.Task.model.Task;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/task")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/")
    public Page<Task> getAllTasks(Authentication authentication, @PageableDefault(size = 10) Pageable pageable) {
        try {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            Long userId = jwt.getClaim("userId");
            if (hasRole(authentication, "ADMIN")) {
                return taskService.getAllTasks(pageable);
            }
            return taskService.getTasksByUserId(userId, pageable);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve tasks: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable Long id) {
        try {
            return taskService.getTaskById(id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve task: " + e.getMessage());
        }
    }

    @PostMapping("/")
    public Task createTask(@Valid @RequestBody CreateTaskRequest request, Authentication authentication) {
        try {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            Long userId = jwt.getClaim("userId");
            return taskService.createTask(request, userId);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Failed to create task: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Task updateTask(@PathVariable Long id, @Valid @RequestBody UpdateTaskRequest request) {
        try {
            return taskService.updateTask(id, request);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Failed to update task: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        try {
            taskService.deleteTask(id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete task: " + e.getMessage());
        }
    }

    private boolean hasRole(Authentication authentication, String role) {
        return authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_" + role));
    }
}
