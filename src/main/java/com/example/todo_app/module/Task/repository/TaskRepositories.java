package com.example.todo_app.module.Task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.todo_app.module.Task.model.Task;

@Repository
public interface TaskRepositories extends JpaRepository<Task, Long> {
}
