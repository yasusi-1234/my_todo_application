package com.example.my_todo_application.task_management.domain.service;

import com.example.my_todo_application.task_management.domain.model.AppUser;
import org.springframework.transaction.annotation.Transactional;

public interface UserRegisterService {
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    AppUser saveAppUser(String firstName, String lastName, String mailAddress, String password);
}
