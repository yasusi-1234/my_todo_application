package com.example.my_todo_application.task_management.domain.reository;

import com.example.my_todo_application.task_management.domain.model.AppUser;
import com.example.my_todo_application.task_management.domain.model.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("TaskRepositoryのテスト")
@Transactional
class TaskRepositoryTest {
    
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private EntityManager entityManager;
    
    @DisplayName("findAllで正しくデータが取得できる")
    void findAllTest() throws Exception {
        List<Task> actual = taskRepository.findAll();
        
        Task actualIndex0 = actual.get(0);
        
        assertAll(
                () -> assertEquals(1L, actual.getTaskId()),
                () -> assertEquals("Yjyycdz5vKA", actual.getTaskName()),
                () -> assertEquals("meMU861oMVYQhsmSj", actual.getDetail()),
                () -> assertEquals(LocalDateTime.of(2021, 12, 29, 0, 0, 0), actual.getStartDatetime()),
                () -> assertEquals(LocalDateTime.of(2022, 1, 29, 0, 0, 0), actual.getEndDatetime()),
                () -> assertEquals(Importance.NOEMAL, actual.getImportance()),
                () -> assertEquals(58 , actual.getProgress()),
                () -> assertEquals(true , actual.getNotice()),
                () -> assertEquals(3L , actual.getAppUser().getAppUserId())
        );

        assertEquals(20, actual.size());
    }

    @DisplayName("findByTaskIdで正しくデータが取得できる")
    @Test
    void findByIdTest() throws Exception {
        Task actual1 = taskRepository.findById(5).orElse(null);

        assertAll(
                () -> assertEquals(5L, actual1.getTaskId()),
                () -> assertEquals("LLweBTirb6ms", actual1.getTaskName()),
                () -> assertEquals(5L, actual1.getAppUser().getAppUserId())
        );
    }

    @DisplayName("saveメソッドで正しく挿入される")
    @Test
    void saveTest() throws Exception {
        Task testTask = taskRepository.findById(1).orElse(null);
        // appUserIdが3のユーザー
        AppUser appUser3 = testTask.getAppUser();

        Task saveTask = createTestTask(appUser3);

        Task savedTask = taskRepository.save(saveTask);

        assertAll(
                () -> assertEquals("test", savedTask.getTaskName()),
                () -> assertEquals("test", savedTask.getDetail()),
                () -> assertEquals(false, savedTask.getProgress()),
                () -> assertEquals(Importance.HIGH, savedTask.getImportance()),
                () -> assertEquals(LocalDateTime.of(2000, 1, 1, 0, 0, 0), savedTask.getStartDatetime()),
                () -> assertEquals(LocalDateTime.of(2001, 2, 3, 1, 2, 3), savedTask.getEndDatetime()),
                () -> assertEquals(3L, savedTask.getAppUser().getAppUserId())
        );

        entityManager.flush();

        Long expectedCount = 21L;
        assertEquals(expectedCount, taskRepository.count());

    }

    @DisplayName("saveメソッドで正しく更新される")
    @Test
    void updateTest() throws Exception {
        Task task = taskRepository.findById(1L).orElse(null);
        task.setTaskName("update");
        taskRepository.save(task);

        entityManager.flush();

        Task updatedTask = taskRepository.findById(1L).orElse(null);
        assertEquals("update", updatedTask.getTaskName());

        Long count = taskRepository.count();
        assertEquals(20L, count);
    }

    private Task createTestTask(AppUser appUser) {
        Task task = new Task();
        task.setNotice(10);
        task.setProgress(1);
        task.setTaskName("test");
        task.setDetail("test");
        task.setImportance(Importance.HIGH);
        task.setStartDateTime(LocalDateTime.of(2000, 1, 1, 0, 0, 0));
        task.setEndtDateTime(LocalDateTime.of(2001, 2, 3, 1, 2, 3));
        task.setAppUser(appUser);
        return task;
    }


}