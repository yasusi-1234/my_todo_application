package com.example.my_todo_application.task_management.domain.service.specification;

import com.example.my_todo_application.task_management.controller.form.Progress;
import com.example.my_todo_application.task_management.domain.model.Importance;
import com.example.my_todo_application.task_management.domain.model.Task;
import com.example.my_todo_application.task_management.domain.reository.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("TaskSpecificationHelperクラスのテスト")
@Transactional
class TaskSpecificationHelperTest {

    @Autowired
    private TaskRepository taskRepository;

    @Test
    @DisplayName("equalAppUserIdは正しくユーザーのIDごとの要素を取得する")
    void equalAppUserIdTest() {
        // 1は5件
        List<Task> actual = taskRepository.findAll(
                Specification.where(
                        TaskSpecificationHelper.fetchUser().and(
                                TaskSpecificationHelper.equalAppUserId(1L))
                )
        );

        assertEquals(5L, actual.size());

        actual.forEach(testObj -> assertEquals(1L, testObj.getAppUser().getAppUserId()));
    }

    @Test
    @DisplayName("betweenDatetimeは引数で与えた間の日付のデータを取得する")
    @Sql(scripts = "classpath:/specification.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void betweenDatetimeTest() {
        List<Task> actual1 = taskRepository.findAll(
                Specification.where(
                        TaskSpecificationHelper.fetchUser().and(
                                TaskSpecificationHelper.betweenDatetime(
                                        LocalDateTime.of(2010, 1, 1, 0, 0, 0),
                                        LocalDateTime.of(2010, 1, 6, 0, 0, 0)
                                        )
                        )
                )
        );

        assertEquals(2, actual1.size());

        List<Task> actual2 = taskRepository.findAll(
                Specification.where(
                        TaskSpecificationHelper.fetchUser().and(
                                TaskSpecificationHelper.betweenDatetime(
                                        LocalDateTime.of(2010, 1, 1, 0, 0, 0),
                                        LocalDateTime.of(2010, 3, 2, 0, 0, 0)
                                )
                        )
                )
        );

        assertEquals(8, actual2.size());
    }

    @Test
    @DisplayName("equalImportanceはimportanceの値から正しく抽出する")
    @Sql(scripts = "classpath:/specification.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void equalImportance() {
        List<Task> actual1 = taskRepository.findAll(
                Specification.where(
                        TaskSpecificationHelper.fetchUser().and(
                                TaskSpecificationHelper.equalImportance(Importance.HIGH)
                        )
                )
        );

        assertEquals(2, actual1.size());

        List<Task> actual2 = taskRepository.findAll(
                Specification.where(
                        TaskSpecificationHelper.fetchUser().and(
                                TaskSpecificationHelper.equalImportance(Importance.VERY_HIGH)
                        )
                )
        );

        assertEquals(1, actual2.size());

        List<Task> actual3 = taskRepository.findAll(
                Specification.where(
                        TaskSpecificationHelper.fetchUser().and(
                                TaskSpecificationHelper.equalImportance(Importance.NORMAL)
                        )
                )
        );

        assertEquals(22, actual3.size());
    }

    @Test
    @DisplayName("equalImportanceとappUserIdの複合テスト")
    @Sql(scripts = "classpath:/specification.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void equalImportanceAndAppUserIdTest() {
        // data.sql のuserId1は 5件 追加のSql5件 計10件
        List<Task> actualUser1 = taskRepository.findAll(
                Specification.where(
                        TaskSpecificationHelper.fetchUser().and(
                                TaskSpecificationHelper.equalAppUserId(1L).and(
                                        TaskSpecificationHelper.equalImportance(Importance.NORMAL)
                                )
                        )
                )
        );
        // appUserIdが1かつImportanceがNORMALのタスクは6件である事の確認
        assertEquals(6, actualUser1.size());
        // appUserIdが全て1である事の確認
        actualUser1.forEach(actualObj -> assertEquals(1L, actualObj.getAppUser().getAppUserId()));

        // data.sql のuserId2は 5件 追加のSql5件 計10件
        List<Task> actualUser2 = taskRepository.findAll(
                Specification.where(
                        TaskSpecificationHelper.fetchUser().and(
                                TaskSpecificationHelper.equalAppUserId(2L).and(
                                        TaskSpecificationHelper.equalImportance(Importance.VERY_LOW)
                                )
                        )
                )
        );

        // appUserIdが2かつImportanceがVERY_LOWのタスクは2件である事の確認
        assertEquals(2, actualUser2.size());
        // appUserIdが全て2である事の確認
        actualUser2.forEach(actualObj -> assertEquals(2, actualObj.getAppUser().getAppUserId()));
    }

    @Nested
    @DisplayName("progressメソッドのテスト")
    class ProgressTest {

        @Test
        @DisplayName("ProgressがRegisterの場合DBのProgressが100の要素を返却するSpecificationを返却する")
        @Sql(scripts = "classpath:/specification.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        void progressRegisterTest() {
            Progress progress = Progress.REGISTER;

            List<Task> actual = taskRepository.findAll(Specification.where(
                    TaskSpecificationHelper.fetchUser().and(
                            TaskSpecificationHelper.progress(progress)
                    )
            ));

            assertEquals(5, actual.size());
        }

        @Test
        @DisplayName("ProgressがWORKINGの場合DBのProgressが1以上~100未満の要素を返却するSpecificationを返却する")
        @Sql(scripts = "classpath:/specification.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        void progressWorkingTest() {
            Progress progress = Progress.WORKING;

            List<Task> actual = taskRepository.findAll(Specification.where(
                    TaskSpecificationHelper.fetchUser().and(
                            TaskSpecificationHelper.progress(progress)
                    )
            ));

            assertEquals(23, actual.size());
        }

        @Test
        @DisplayName("ProgressがNOTSTERTの場合DBのProgressが0の要素を返却するSpecificationを返却する")
        @Sql(scripts = "classpath:/specification.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        void progressNotStartTest() {
            Progress progress = Progress.NOT_START;

            List<Task> actual = taskRepository.findAll(Specification.where(
                    TaskSpecificationHelper.fetchUser().and(
                            TaskSpecificationHelper.progress(progress)
                    )
            ));

            assertEquals(2, actual.size());
        }

    }
}