package com.example.todo_app.module.User.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.todo_app.module.User.model.User;

@Repository
public interface UserRepositories extends JpaRepository<User, Long> {

}