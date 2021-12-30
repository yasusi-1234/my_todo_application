package com.example.my_todo_application.task_management.domain.reository;

import com.example.my_todo_application.task_management.domain.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
