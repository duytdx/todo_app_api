package com.example.todo_app.module.Task.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import com.example.todo_app.module.Task.repository.TaskRepositories;
import com.example.todo_app.module.Task.model.Task;
import java.util.List;

@Service
public class TaskService {
    private final TaskRepositories taskRepositories;

    public TaskService(TaskRepositories taskRepositories) {
        this.taskRepositories = taskRepositories;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<Task> getAllTasks() {
        return taskRepositories.findAll();
    }

    public List<Task> getTasksByUserId(Long userId) {
        return taskRepositories.findByCreateById(userId);
    }

    public Task getTaskById(Long id) {
        return taskRepositories.findById(id).orElse(null);
    }

    public Task createTask(Task task) {
        return taskRepositories.save(task);
    }

    public Task updateTask(Long id, Task taskDetails) {
        Task task = taskRepositories.findById(id).orElse(null);
        if (task != null) {
            task.setTitle(taskDetails.getTitle());
            task.setDescription(taskDetails.getDescription());
            task.setCompleted(taskDetails.isCompleted());
            return taskRepositories.save(task);
        }
        return null;
    }

    public void deleteTask(Long id) {
        taskRepositories.deleteById(id);
    }
}
