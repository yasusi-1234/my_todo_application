package com.example.my_todo_application.task_management.controller;

import com.example.my_todo_application.security.service.AppUserDetailsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("ログインコントローラーのテスト")
@WebMvcTest(controllers = LoginController.class,
includeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = { AppUserDetailsService.class }
))
// jpa関連のDIの設定
@AutoConfigureDataJpa
@Transactional
class LoginControllerTest {


    @Autowired
    MockMvc mockMvc;


    @DisplayName("getLoginメソッドのテスト")
    @Nested
    class GetLoginTest {

        @DisplayName("ログイン状態でもアクセス可能")
        @WithUserDetails(
                value = "6TM8ytI8xvJU@xxx.xx.xx",
                userDetailsServiceBeanName = "appUserDetailsService")
        @Test
        void getLoginUserTest() throws Exception {
            mockMvc.perform(get("/login"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("login/login"));
        }

        @DisplayName("誰でもアクセス可能")
        @WithAnonymousUser
        @Test
        void getLoginAnonymousTest() throws Exception {
            mockMvc.perform(get("/login"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("login/login"));
        }

    }


    @DisplayName("ログインリクエストのテスト")
    @Nested
    class PostLoginTest {

        @DisplayName("ログイン成功の場合")
        @WithAnonymousUser
        @Test
        void successLoginTest() throws Exception {
            mockMvc.perform(post("/login")
                    .with(csrf())
                    .param("username", "6TM8ytI8xvJU@xxx.xx.xx")
                    .param("password", "password")
            ).andExpect(status().isFound())
                    .andExpect(redirectedUrl("/task/home"));
        }

        @DisplayName("ログイン失敗の場合")
        @WithAnonymousUser
        @Test
        void failLoginTest() throws Exception {
            mockMvc.perform(post("/login")
                    .with(csrf())
                    .param("username", "none")
                    .param("password", "pass")
            ).andExpect(status().isFound())
                    .andExpect(redirectedUrl("/login?error=true"));
        }
    }

}