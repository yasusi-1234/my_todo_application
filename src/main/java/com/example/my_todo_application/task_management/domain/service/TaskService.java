package com.example.my_todo_application.task_management.domain.service;

import com.example.my_todo_application.task_management.controller.form.Progress;
import com.example.my_todo_application.task_management.domain.model.Importance;
import com.example.my_todo_application.task_management.domain.model.Task;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface TaskService {

    Task findByTaskId(Long taskId);

    List<Task> findAllTaskByUserId(Long userId);

    Task saveTask(Task task);

    List<Task> findTasksOf(
            Long userId,
            String taskName,
            LocalDate startDate,
            LocalDate endDate,
            List<Importance> importance,
            Progress progress);

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    void deleteTaskIdAndUserId(long taskId, long userId);
}
