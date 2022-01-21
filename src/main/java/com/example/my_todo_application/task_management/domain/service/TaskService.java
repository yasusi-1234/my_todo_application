package com.example.my_todo_application.task_management.domain.service;

import com.example.my_todo_application.task_management.controller.form.Progress;
import com.example.my_todo_application.task_management.domain.model.Importance;
import com.example.my_todo_application.task_management.domain.model.Task;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskService {

    Task findByTaskId(Long taskId);

    List<Task> findAllTaskByUserId(Long userId);

    Task saveTask(Task task);

    List<Task> findTasksOf(
            Long userId,
            String taskName,
            LocalDateTime startTime,
            LocalDateTime endTime,
            Importance importance,
            Progress progress);

    List<Task> findTasksOf(
            Long userId,
            Boolean notice,
            LocalDateTime targetDateTime);

    void deleteTaskIdAndUserId(long taskId, long userId);
}
