package com.example.my_todo_application.task_management.controller;

import com.example.my_todo_application.security.service.AppUserDetailsService;
import com.example.my_todo_application.task_management.controller.form.Progress;
import com.example.my_todo_application.task_management.controller.form.TaskRegisterForm;
import com.example.my_todo_application.task_management.domain.model.Importance;
import com.example.my_todo_application.task_management.domain.model.Task;
import com.example.my_todo_application.task_management.domain.service.TaskService;
import com.example.my_todo_application.task_management.domain.service.TaskServiceImpl;
import com.example.my_todo_application.task_management.domain.service.exception.TaskNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TaskControllerTestクラスのテスト")
@WebMvcTest(controllers =TaskController.class,
        includeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = { AppUserDetailsService.class, TaskServiceImpl.class}
        ))
// jpa関連のDIの設定
@AutoConfigureDataJpa
@Transactional
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
//                        doReturn(expected2).when(taskService).findTasksOf(anyLong(), anyBoolean(), any(LocalDateTime.class));

                        mockMvc.perform(get("/task/home"))
                                .andExpect(status().isOk())
                                .andExpect(view().name("task/task-home"))
                                .andExpect(model().attribute("username", "青山"))
                                .andExpect(model().attribute("userTasks", expected1))
//                                .andExpect(model().attribute("noticeTasks", expected2))
                                // controller全体で共有しているmodel
                                .andExpect(model().attribute("progress", Progress.values()))
                                .andExpect(model().attribute("importance", Importance.values()));

                        verify(taskService, times(1)).findAllTaskByUserId(argumentCaptor.capture());
//                        verify(taskService, times(1)).findTasksOf(anyLong(), anyBoolean(), any(LocalDateTime.class));

                        assertEquals(1L, argumentCaptor.getValue());
                }
        }

        @Nested
        @DisplayName("postRegisterTaskメソッドのテスト")
        class PostRegisterTaskTest {

                @DisplayName("未認証のユーザーはアクセス不可")
                @WithAnonymousUser
                @Test
                void postRegisterTaskAnonymousUser() throws Exception {
                        mockMvc.perform(get("/task/register"))
                                .andExpect(status().isFound())
                                .andExpect(redirectedUrlPattern("**/login"));
                }

                @DisplayName("フォーム入力値に異常があった場合")
                @WithUserDetails(value = "6TM8ytI8xvJU@xxx.xx.xx",
                        userDetailsServiceBeanName = "appUserDetailsService")
                @Test
                void postRegisterFailTaskWithUser() throws Exception {
                        // 異常のあるフォーム
                        TaskRegisterForm badForm = badRegisterForm();

                        mockMvc.perform(post("/task/register")
                                        .flashAttr("taskRegisterForm", badForm)
                                        .with(csrf()))
                                .andExpect(status().isOk())
                                .andExpect(view().name("task/task-home"))
                                .andDo(print())
                                .andExpect(model().hasErrors())
                                .andExpect(model().errorCount(3));

                        verify(taskService, never()).saveTask(any());
                }

                @DisplayName("フォーム入力値が正常の場合")
                @WithUserDetails(value = "6TM8ytI8xvJU@xxx.xx.xx",
                        userDetailsServiceBeanName = "appUserDetailsService")
                @Test
                void postRegisterSuccessTaskWithUser() throws Exception {
                        // 異常のないフォーム
                        TaskRegisterForm form = normalRegisterForm();
                        Task task = new Task();
                        doReturn(task).when(taskService).saveTask(any(Task.class));

                        mockMvc.perform(post("/task/register")
                                        .flashAttr("taskRegisterForm", form)
                                        .with(csrf()))
                                .andExpect(status().isFound())
                                .andDo(print())
                                .andExpect(model().hasNoErrors())
                                .andExpect(redirectedUrlPattern("**/task/home"))
                                .andExpect(flash().attribute("complete", "新規タスクを登録しました"));

                        verify(taskService, times(1)).saveTask(any(Task.class));
                }


        }

        @Nested
        @DisplayName("postDeleteTaskメソッドのテスト")
        class PostDeleteTaskTest {
                @DisplayName("未認証のユーザーはアクセス不可")
                @WithAnonymousUser
                @Test
                void postDeleteTaskAnonymousUser() throws Exception {
                        mockMvc.perform(post("/task/delete")
                                        .param("taskId", "1")
                                        .with(csrf()))
                                .andExpect(status().isFound())
                                .andExpect(redirectedUrlPattern("**/login"));
                }

                @DisplayName("正常系の処理")
                @WithUserDetails(value = "6TM8ytI8xvJU@xxx.xx.xx",
                        userDetailsServiceBeanName = "appUserDetailsService")
                @Test
                void postDeleteTaskWithUserSuccess() throws Exception {
                        mockMvc.perform(post("/task/delete")
                                        .param("taskId", "1")
                                        .with(csrf()))
                                .andExpect(status().isFound())
                                .andExpect(redirectedUrlPattern("**/task/home"))
                                .andExpect(flash().attribute("complete", "タスクの削除が完了しました"));

                        verify(taskService, times(1)).deleteTaskIdAndUserId(1L, 1L);
                }

                @DisplayName("異常系の処理")
                @WithUserDetails(value = "6TM8ytI8xvJU@xxx.xx.xx",
                        userDetailsServiceBeanName = "appUserDetailsService")
                @Test
                void postDeleteTaskWithUserFail() throws Exception {

                        TaskNotFoundException ex = new TaskNotFoundException("TaskNotFoundException occur");

                        doThrow(ex).when(taskService).deleteTaskIdAndUserId(anyLong(), anyLong());

                        mockMvc.perform(post("/task/delete")
                                        .param("taskId", "1")
                                        .with(csrf()))
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(view().name("error/400"))
                                .andExpect(model().attribute("message", ex.getMessage()));

                        verify(taskService, times(1)).deleteTaskIdAndUserId(1L, 1L);
                }
        }

        private TaskRegisterForm badRegisterForm(){
                TaskRegisterForm form = new TaskRegisterForm();
                form.setTaskName("");
                form.setStartDate(LocalDate.now().minusDays(1));
                form.setEndDate(LocalDate.now());
                form.setImportance(null);
                form.setProgress(-1);
                return form;
        }

        private TaskRegisterForm normalRegisterForm(){
                TaskRegisterForm form = new TaskRegisterForm();
                form.setTaskName("this is task");
                form.setStartDate(LocalDate.now().minusDays(1));
                form.setEndDate(LocalDate.now());
                form.setImportance(Importance.NORMAL);
                form.setProgress(55);
                form.setNotice(true);
                return form;
        }


}