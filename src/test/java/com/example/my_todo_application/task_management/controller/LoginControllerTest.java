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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@DisplayName("ログインコントローラーのテスト")
@WebMvcTest(controllers = LoginController.class,
includeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = { AppUserDetailsService.class }
))
// jpa関連のDIの設定
@AutoConfigureDataJpa
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

}