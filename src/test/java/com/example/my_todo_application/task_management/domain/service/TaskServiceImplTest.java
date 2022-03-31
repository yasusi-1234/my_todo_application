package com.example.my_todo_application.task_management.domain.service;


import com.example.my_todo_application.task_management.controller.form.Progress;
import com.example.my_todo_application.task_management.domain.model.Importance;
import com.example.my_todo_application.task_management.domain.model.Task;
import com.example.my_todo_application.task_management.domain.repository.TaskRepository;
import com.example.my_todo_application.task_management.domain.service.exception.TaskNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = TaskServiceImpl.class)
@DisplayName("TaskServiceImplクラスのテスト")
class TaskServiceImplTest {

    @Autowired
    private TaskServiceImpl taskService;

    @MockBean
    private TaskRepository taskRepository;

    @Test
    @DisplayName("findByTaskIdTestはtaskRepositoryからタスクを取得し返却する")
    void findByTaskIdTest() {
        // setUp
        Task testTask = createTask();
        Optional<Task> testTaskOpt = Optional.of(testTask);
        doReturn(testTaskOpt).when(taskRepository).findById(anyLong());

        Task actual = taskService.findByTaskId(1L);

        assertEquals(testTask, actual);

        verify(taskRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("findAllTaskByUserIdはtaskRepositoryからタスクリストを取得し返却する")
    void findAllTaskByUserIdTest() {
        // setup
        List<Task> tasks = createTasks(5);
        doReturn(tasks).when(taskRepository).findAll(ArgumentMatchers.<Specification<Task>>any());

        List<Task> actual = taskService.findAllTaskByUserId(1L);

        assertEquals(tasks, actual);

        verify(taskRepository, times(1)).findAll(ArgumentMatchers.<Specification<Task>>any());
    }

    @Test
    @DisplayName("saveTaskが正しく実行される")
    void saveTaskTest() {
        // setup
        Task savedTask = createTask();
        Task saveTask = createTask();
        saveTask.setTaskId(null);
        doReturn(savedTask).when(taskRepository).save(saveTask);

        // action
        Task actual = taskService.saveTask(saveTask);

        assertEquals(savedTask, actual);
        verify(taskRepository, times(1)).save(saveTask);
    }

    @Nested
    @DisplayName("deleteByTaskIdAndUserId")
    class DeleteByTaskIdAndUserIdTest {

        @Test
        @DisplayName("deleteByTaskIdAndUserIdで既存のタスク情報が見つかった場合はdelete処理が行われる")
        void deleteTaskTestOfExistTask() {
            // setUp
            Optional<Task> taskOpt = Optional.of(new Task());
            doReturn(taskOpt).when(taskRepository).findOne(ArgumentMatchers.<Specification<Task>>any());

            // action
            taskService.deleteTaskIdAndUserId(1L, 1L);

            verify(taskRepository, times(1)).findOne(ArgumentMatchers.<Specification<Task>>any());
            verify(taskRepository, times(1)).delete(taskOpt.get());
        }

        @Test
        @DisplayName("deleteByTaskIdAndUserIdで既存のタスク情報が無かった場合はdelete処理は" +
                "行われずTaskNotFoundException例外を投げる")
        void deleteTaskTestOfEmptyTask() {
            // setUp
            long taskId = 1L;
            Optional<Task> taskOpt = Optional.empty();
            doReturn(taskOpt).when(taskRepository).findOne(ArgumentMatchers.<Specification<Task>>any());

            // action
            TaskNotFoundException e = assertThrows(
                    TaskNotFoundException.class,
                    () -> taskService.deleteTaskIdAndUserId(taskId, 1L));

            assertEquals("指定されたTaskIdは存在しません: 指定されたTaskId -> " + taskId, e.getMessage());

            verify(taskRepository, times(1)).findOne(ArgumentMatchers.<Specification<Task>>any());
            verify(taskRepository, never()).delete(any(Task.class));
        }

    }

    @Test
    @DisplayName("findTasksOfメソッドが正しく実行される")
    void findTasksOf() {
//        findTasksOf(taskName, fromDate, toDate, importance, progress) : List<Task>
        // setUp
        List<Task> tasks = createTasks(10);
        doReturn(tasks).when(taskRepository).findAll(ArgumentMatchers.<Specification<Task>>any());

        List<Task> actual =
                taskService.findTasksOf(
                        1L, "test", LocalDate.now(), LocalDate.now().plusDays(1), Arrays.asList(Importance.values()), Progress.ALL);
        assertEquals(tasks, actual);

    }

    private static Task createTask(){
        Task task = new Task();
        task.setTaskId(1L);
        task.setTaskName("test");
        task.setDetail("this is test");
        task.setImportance(Importance.NORMAL);
        task.setStartDate(LocalDate.of(2010, 1, 1));
        task.setEndDate(LocalDate.of(2011, 1, 2));
        task.setNotice(false);
        task.setProgress(10);

        return task;
    }

    private static List<Task> createTasks(int createNum) {
        return Stream.iterate(0L, i -> i + 1).limit(createNum).map(i -> {
            Task task = new Task();
            task.setTaskId(i);
            return task;
        }).collect(Collectors.toList());
    }
}