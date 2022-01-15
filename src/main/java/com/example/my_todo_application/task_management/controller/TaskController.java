package com.example.my_todo_application.task_management.controller;

import com.example.my_todo_application.task_management.domain.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("task")
@RequiredArgsConstructor
public class TaskController {


    private final TaskService taskService;


}
