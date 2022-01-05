package com.example.my_todo_application.task_management.domain.service;

import com.example.my_todo_application.task_management.controller.form.Progress;
import com.example.my_todo_application.task_management.domain.model.Importance;
import com.example.my_todo_application.task_management.domain.model.Task;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskService {

    Task findByTaskId(Long taskId);

    List<Task> findAllTaskByUserId(Long userId);

    Task saveTask(Task task);

    void deleteByTaskId(Long taskId);

    List<Task> findTasksOf(
            String taskName,
            LocalDateTime startTime,
            LocalDateTime endTime,
            Importance importance,
            Progress progress);
}
