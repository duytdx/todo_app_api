package com.example.todo_app.module.Task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.todo_app.module.Task.model.Task;
import com.example.todo_app.module.User.model.User;
import java.util.List;

@Repository
public interface TaskRepositories extends JpaRepository<Task, Long> {
    List<Task> findByCreateBy(User user);
    List<Task> findByCreateById(Long userId);
}
