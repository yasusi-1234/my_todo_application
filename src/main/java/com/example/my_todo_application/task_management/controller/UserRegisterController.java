package com.example.my_todo_application.task_management.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("user")
public class UserRegisterController {

    @GetMapping("register")
    public String getUserRegister(){

        return "user/user-register";
    }

    @PostMapping("register")
    public String postUserRegister(){
        return "user/user-register";
    }
}
