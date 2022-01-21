package com.example.my_todo_application.task_management.controller;

import com.example.my_todo_application.task_management.domain.service.exception.TaskNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ToDoControllerAdvice {

    @ExceptionHandler(TaskNotFoundException.class)
    public String taskNotFoundExceptionHandler(
            TaskNotFoundException e,
            Model model
    ) {
        model.addAttribute("message", e.getMessage());
        return "error/400";
    }
}
