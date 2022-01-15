package com.example.my_todo_application.task_management.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ログインコントローラーのテスト")
@WebMvcTest
class LoginControllerTest {


    @Autowired
    MockMvc mockMvc;

    @DisplayName("getLoginメソッドのテスト")
    @Nested
    class GetLoginTest {

        @WithUser
        void test() {

        }

    }
}