package com.example.my_todo_application.task_management.domain.reository;

import com.example.my_todo_application.task_management.domain.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    AppUser findByMailAddressAndPassword(String mailAddress, String password);
}
