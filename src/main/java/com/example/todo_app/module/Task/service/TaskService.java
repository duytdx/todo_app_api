package com.example.todo_app.module.Task.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import com.example.todo_app.module.Task.repository.TaskRepositories;
import com.example.todo_app.module.Task.dto.CreateTaskRequest;
import com.example.todo_app.module.Task.dto.UpdateTaskRequest;
import com.example.todo_app.module.Task.model.Task;
import com.example.todo_app.module.User.model.User;
import com.example.todo_app.module.User.repository.UserRepositories;
import java.util.List;

@Service
public class TaskService {
    private final TaskRepositories taskRepositories;
    private final UserRepositories userRepositories;

    public TaskService(TaskRepositories taskRepositories, UserRepositories userRepositories) {
        this.taskRepositories = taskRepositories;
        this.userRepositories = userRepositories;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<Task> getAllTasks() {
        try {
            return taskRepositories.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving tasks from database", e);
        }
    }

    public List<Task> getTasksByUserId(Long userId) {
        try {
            return taskRepositories.findByCreateById(userId);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving tasks for user: " + userId, e);
        }
    }

    public Task getTaskById(Long id) {
        try {
            return taskRepositories.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + id));
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving task from database", e);
        }
    }

    public Task createTask(CreateTaskRequest request, Long userId) {
        try {
            User user = userRepositories.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

            Task newTask = new Task();
            newTask.setTitle(request.title());
            newTask.setDescription(request.description());
            newTask.setCompleted(request.completed());
            newTask.setCreateBy(user);
            return taskRepositories.save(newTask);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error creating task: " + e.getMessage(), e);
        }
    }

    public Task updateTask(Long id, UpdateTaskRequest request) {
        try {
            Task task = taskRepositories.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + id));

            task.setTitle(request.title());
            task.setDescription(request.description());
            task.setCompleted(request.completed());
            return taskRepositories.save(task);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error updating task: " + e.getMessage(), e);
        }
    }

    public void deleteTask(Long id) {
        try {
            if (!taskRepositories.existsById(id)) {
                throw new IllegalArgumentException("Task not found with id: " + id);
            }
            taskRepositories.deleteById(id);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error deleting task: " + e.getMessage(), e);
        }
    }
}
