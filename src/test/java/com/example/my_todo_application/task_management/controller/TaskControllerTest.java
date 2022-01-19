package com.example.my_todo_application.task_management.controller;

import com.example.my_todo_application.security.service.AppUserDetailsService;
import com.example.my_todo_application.task_management.domain.model.Task;
import com.example.my_todo_application.task_management.domain.service.TaskService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TaskControllerTestクラスのテスト")
@WebMvcTest(controllers =TaskController.class,
        includeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = { AppUserDetailsService.class }
        ))
// jpa関連のDIの設定
@AutoConfigureDataJpa
class TaskControllerTest {

        @Autowired
        MockMvc mockMvc;

        @MockBean
        TaskService taskService;

        @DisplayName("getTaskHomeのテスト")
        @Nested
        class GetTaskHomeTest {

                @Test
                @WithAnonymousUser
                @DisplayName("ログインしていないユーザーはlogin画面にリダイレクトされる")
                void getTaskHomeAnonymousUser() throws Exception {
                        mockMvc.perform(get("/task/home"))
                                .andExpect(status().isFound())
                                .andExpect(redirectedUrlPattern("**/login"));
                }

                @Test
                @WithUserDetails(value = "6TM8ytI8xvJU@xxx.xx.xx",
                        userDetailsServiceBeanName = "appUserDetailsService")
                @DisplayName("ログインしているユーザーがアクセスした場合")
                void getTaskHomeWithUser() throws Exception {
                        // setup
                        List<Task> expected1 = Stream.of(new Task(), new Task(), new Task()).collect(Collectors.toList());
                        List<Task> expected2 = Stream.of(new Task(), new Task()).collect(Collectors.toList());

                        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);

                        doReturn(expected1).when(taskService).findAllTaskByUserId(anyLong());
                        doReturn(expected2).when(taskService).findTasksOf(anyLong(), anyBoolean(), any(LocalDateTime.class));

                        mockMvc.perform(get("/task/home"))
                                .andExpect(status().isOk())
                                .andExpect(view().name("task/task-home"))
                                .andExpect(model().attribute("username", "青山"))
                                .andExpect(model().attribute("userTasks", expected1))
                                .andExpect(model().attribute("noticeTasks", expected2));

                        verify(taskService, times(1)).findAllTaskByUserId(argumentCaptor.capture());
                        verify(taskService, times(1)).findTasksOf(anyLong(), anyBoolean(), any(LocalDateTime.class));

                        assertEquals(1L, argumentCaptor.getValue());
                }
        }

}