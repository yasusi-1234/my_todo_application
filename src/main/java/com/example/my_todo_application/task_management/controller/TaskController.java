package com.example.my_todo_application.task_management.controller;

import com.example.my_todo_application.task_management.controller.form.Progress;
import com.example.my_todo_application.task_management.controller.form.TaskRegisterForm;
import com.example.my_todo_application.task_management.controller.form.TaskSearchForm;
import com.example.my_todo_application.task_management.domain.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("task")
@RequiredArgsConstructor
public class TaskController {


    private final TaskService taskService;


    @GetMapping("home")
    public String getTaskHome(
            @ModelAttribute("taskSearchForm") TaskSearchForm taskSearchForm,
            @ModelAttribute("TaskRegisterFrom")TaskRegisterForm taskRegisterForm,
            Model model){
        model.addAttribute("progress", Progress.values());

        return "task/task-home";
    }

}
