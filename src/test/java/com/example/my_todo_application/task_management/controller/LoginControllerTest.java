package com.example.my_todo_application.task_management.controller;

import com.example.my_todo_application.MyTodoApplication;
import com.example.my_todo_application.security.service.AppUserDetailsService;
import com.example.my_todo_application.task_management.domain.repository.AppUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@DisplayName("ログインコントローラーのテスト")
//@WebMvcTest(controllers = LoginController.class)
@WebMvcTest(controllers = LoginController.class,
includeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = { AppUserDetailsService.class }
))
class LoginControllerTest {


    @Autowired
    MockMvc mockMvc;


    @DisplayName("getLoginメソッドのテスト")
    @Nested
    class GetLoginTest {

        @DisplayName("ログイン状態でもアクセス可能")
        @WithUserDetails(
                setupBefore = TestExecutionEvent.TEST_EXECUTION,
                value = "6TM8ytI8xvJU@xxx.xx.xx",
                userDetailsServiceBeanName = "appUserDetailsService")
        @Test
        void getLoginUserTest() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/login"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.view().name("login/login"));
        }

        @DisplayName("誰でもアクセス可能")
        @WithAnonymousUser
        @Test
        void getLoginAnonymousTest() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/login"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.view().name("login/login"));
        }

    }

    @ComponentScan({"com.example.my_todo_application.task_management.domain.repository",
            "com.example.my_todo_application.security.service"})
    static class Config{}
}