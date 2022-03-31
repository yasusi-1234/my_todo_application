package com.example.my_todo_application.task_management.domain.service.specification;

import com.example.my_todo_application.task_management.controller.form.Progress;
import com.example.my_todo_application.task_management.domain.model.Importance;
import com.example.my_todo_application.task_management.domain.model.Task;
import com.example.my_todo_application.task_management.domain.repository.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("TaskSpecificationHelperクラスのテスト")
@Transactional
class TaskSpecificationHelperTest {

    @Autowired
    private TaskRepository taskRepository;

    @Test
    @DisplayName("likeTaskNameが指定したタスク名を含む要素を返却する")
    void likeTaskNameTest() {
        int expectedCount = 6;
        int actual = taskRepository.findAll(Specification.where(
                TaskSpecificationHelper.fetchUser().and(
                        TaskSpecificationHelper.likeTaskName("v")
                )
        )).size();
        assertEquals(expectedCount, actual);
    }

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

    @Nested
    @DisplayName("日付の抽出に関するメソッドのテスト")
    @Sql(scripts = "classpath:/specification.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    class DateAfterAndBefore {

        @Test
        @DisplayName("afterDateメソッドは引数に与えた日付以降の終了日を持つタスクを抽出するSpecificationを返却する")
        void afterDateTest() {

            LocalDate startDate = LocalDate.of(2023, 3, 14);
            List<Task> actual = taskRepository.findAll(
                    Specification.where(
                            TaskSpecificationHelper.afterDate(startDate)
                    )
            );

            int expected = 4;
            assertEquals(expected, actual.size());

            List<String> taskNames = List
                    .of("0OLNL2C0gsl5hMNN", "cowhgvKF3cv2", "RNx095v4aCtuO", "RTytr4ZvYVK");

            actual.forEach( actualTaskName ->
                    assertTrue(taskNames.contains(actualTaskName.getTaskName()))
            );
        }

        @Test
        @DisplayName("beforeDateメソッドは引数に与えた日付以前の開始日を持つタスクを抽出するSpecificationを返却する")
        void beforeDateTest() {

            LocalDate endDate = LocalDate.of(2021, 12, 29);
            List<Task> actual = taskRepository.findAll(
                    Specification.where(
                            TaskSpecificationHelper.beforeDate(endDate)
                    )
            );

            int expected = 20;
            assertEquals(expected, actual.size());
        }

        @Test
        @DisplayName("beforeDateメソッドとafterDateの組み合わせで" +
                "期待したTaskオブジェクトを取得できる")
        void beforeDateAndAfterDateTest() {

            LocalDate startDate = LocalDate.of(2023, 3, 6);
            LocalDate endDate = LocalDate.of(2023, 3, 14);
            List<Task> actual = taskRepository.findAll(
                    Specification.where(
                            TaskSpecificationHelper.beforeDate(endDate)
                    ).and(
                            TaskSpecificationHelper.afterDate(startDate)
                    )
            );

            int expected = 4;
            assertEquals(expected, actual.size());
        }

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

    @Nested
    @DisplayName("inImportanceメソッドのテスト")
    @Sql(scripts = "classpath:/specification.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    class InImportanceTest {

        @DisplayName("全てのImportanceを指定すると全てのTaskを取得できる")
        @Test
        void inImportanceAllTest() {
            List<Importance> importanceList = Arrays.asList(Importance.values());

            List<Task> actual = taskRepository.findAll(
                    Specification.where(
                            TaskSpecificationHelper.inImportance(importanceList)
                    )
            );

            int expectedSize = 30;

            assertEquals(expectedSize, actual.size());
        }

        @DisplayName("引数にHighとVeryHighのImportanceListを指定するとHighとVeryHighのImportanceを含むTaskを取得できる")
        @Test
        void inImportanceHighAndVeryHigh() {
            List<Importance> importanceList = Arrays.asList(Importance.HIGH, Importance.VERY_HIGH);

            List<Task> actual = taskRepository.findAll(
                    Specification.where(
                            TaskSpecificationHelper.inImportance(importanceList)
                    )
            );

            int expectedSize = 3;

            assertEquals(expectedSize, actual.size());

            actual.forEach(act -> assertTrue(importanceList.contains(act.getImportance())));
        }
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
    @Sql(scripts = "classpath:/specification.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    class ProgressTest {

        @Test
        @DisplayName("ProgressがRegisterの場合DBのProgressが100の要素を返却するSpecificationを返却する")
        void progressRegisterTest() {
            Progress progress = Progress.REGISTER;

            List<Task> actual = taskRepository.findAll(Specification.where(
                    TaskSpecificationHelper.fetchUser().and(
                            TaskSpecificationHelper.progress(progress)
                    )
            ));

            int expected = 5;

            assertEquals(expected, actual.size());
        }

        @Test
        @DisplayName("ProgressがNOT_REGISTERの場合DBのProgressが100以外の要素を返却するSpecificationを返却する")
        void progressNotRegisterTest() {
            Progress progress = Progress.NOT_REGISTER;

            List<Task> actual = taskRepository.findAll(Specification.where(
                    TaskSpecificationHelper.fetchUser().and(
                            TaskSpecificationHelper.progress(progress)
                    )
            ));

            int expected = 25;

            assertEquals(expected, actual.size());
        }

        @Test
        @DisplayName("ProgressがALLの場合全ての要素が取得される")
        void progressAllTest() {
            Progress progress = Progress.ALL;

            List<Task> actual = taskRepository.findAll(Specification.where(
                    TaskSpecificationHelper.fetchUser().and(
                            TaskSpecificationHelper.progress(progress)
                    )
            ));

            int expected = 30;

            assertEquals(expected, actual.size());
        }

        @Test
        @DisplayName("Progressがnullの場合全ての要素が取得される")
        void progressNullTest() {

            List<Task> actual = taskRepository.findAll(Specification.where(
                    TaskSpecificationHelper.fetchUser().and(
                            TaskSpecificationHelper.progress(null)
                    )
            ));

            int expected = 30;

            assertEquals(expected, actual.size());
        }

    }

    @Nested
    @DisplayName("inNoticeメソッドのテスト")
    class NoticeTest {

        @Test
        @DisplayName("inNoticeはtrueでDBのnoticeが1の要素を返却する")
        @Sql(scripts = "classpath:/specification.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        void inNoticeTrueTest() {
            List<Task> actual = taskRepository.findAll(
                    Specification.where(
                            TaskSpecificationHelper.fetchUser().and(
                                    TaskSpecificationHelper.inNotice(true)
                            )
                    )
            );

            assertEquals(8, actual.size());
        }

        @Test
        @DisplayName("inNoticeはfalseでDBのnoticeが0の要素を返却する")
        @Sql(scripts = "classpath:/specification.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        void inNoticeFalseTest() {
            List<Task> actual = taskRepository.findAll(
                    Specification.where(
                            TaskSpecificationHelper.fetchUser().and(
                                    TaskSpecificationHelper.inNotice(false)
                            )
                    )
            );

            assertEquals(22, actual.size());
        }

        @Test
        @DisplayName("inNoticeはnullでDBの全要素を返却する")
        @Sql(scripts = "classpath:/specification.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        void inNoticeNullTest() {
            List<Task> actual = taskRepository.findAll(
                    Specification.where(
                            TaskSpecificationHelper.fetchUser().and(
                                    TaskSpecificationHelper.inNotice(null)
                            )
                    )
            );

            assertEquals(30, actual.size());
        }

    }

    @Nested
    @DisplayName("inTaskDateTimeメソッドのテスト")
    class InDateTimeTest {

        @DisplayName("inTaskDateTimeは指定された日付がDBのTaskテーブルのstartTimeとendTimeの間に位置するTask情報を返却する")
        @Sql(scripts = "classpath:/specification.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        void inTaskDateTime1 () {
            // 日付に2010-01-06 00:00:00が指定された場合
            LocalDateTime ldt = LocalDateTime.of(2010, 1, 6, 0, 0, 0);
            List<Task> actual = taskRepository.findAll(
                    Specification.where(
                            TaskSpecificationHelper.fetchUser().and(
                                    TaskSpecificationHelper.inTaskDateTime(ldt)
                            )
                    )
            );

            assertEquals(2, actual.size());
        }

        @DisplayName("inTaskDateTimeは指定された日付がDBのTaskテーブルのstartTimeとendTimeの間に位置するTask情報を返却する")
        @Sql(scripts = "classpath:/specification.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        void inTaskDateTime2 () {
            // 日付に2010-03-04 00:00:00が指定された場合
            LocalDateTime ldt = LocalDateTime.of(2010, 1, 6, 0, 0, 0);
            List<Task> actual = taskRepository.findAll(
                    Specification.where(
                            TaskSpecificationHelper.fetchUser().and(
                                    TaskSpecificationHelper.inTaskDateTime(ldt)
                            )
                    )
            );

            assertEquals(3, actual.size());
        }

        @DisplayName("inTaskDateTimeは指定された日付がnullの場合は全要素を返却する")
        @Sql(scripts = "classpath:/specification.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        void inTaskDateTimeIsNull () {
            List<Task> actual = taskRepository.findAll(
                    Specification.where(
                            TaskSpecificationHelper.fetchUser().and(
                                    TaskSpecificationHelper.inTaskDateTime(null)
                            )
                    )
            );

            assertEquals(30, actual.size());
        }

    }

    @Nested
    @DisplayName("equalTaskIdメソッドのテスト")
    class EqualTaskIdTest {

        @DisplayName("equalTaskIdが正しいSpecificationを返却する 値が存在する場合")
        @Test
        void canGetOneTaskWithEqualTaskId() {
            Optional<Task> actualOpt = taskRepository.findOne(
                    Specification.where(
                            TaskSpecificationHelper.fetchUser().and(
                                    TaskSpecificationHelper.equalTaskId(1L)
                            )

                    )
            );

            assertTrue(actualOpt.isPresent());

            Task actual = actualOpt.orElse(new Task());

            assertEquals("Yjyycdz5vKA", actual.getTaskName());
        }


        @DisplayName("equalTaskIdが正しいSpecificationを返却する 値が存在しない場合")
        @Test
        void canGetOneTaskWithEqualTaskIdEmpty() {
            Optional<Task> actualOpt = taskRepository.findOne(
                    Specification.where(
                            TaskSpecificationHelper.fetchUser().and(
                                    TaskSpecificationHelper.equalTaskId(100L)
                            )

                    )
            );

            assertTrue(actualOpt.isEmpty());

        }

        @DisplayName("equalTaskIdがequalAppUserIdと合わせても正しいSpecificationを返却する 値が存在する場合")
        @Test
        void equalTaskIdAndEqualUserIdOfOneTask() {
            Optional<Task> actualOpt = taskRepository.findOne(
                    Specification.where(
                            TaskSpecificationHelper.fetchUser().and(
                                    TaskSpecificationHelper.equalTaskId(1L).and(
                                            TaskSpecificationHelper.equalAppUserId(3L)
                                    )
                            )

                    )
            );

            assertTrue(actualOpt.isPresent());

            Task actual = actualOpt.orElse(new Task());

            assertAll(
                    () -> assertEquals("Yjyycdz5vKA", actual.getTaskName()),
                    () -> assertEquals("永田", actual.getAppUser().getLastName())
            );


        }

        @DisplayName("equalTaskIdがequalAppUserIdと合わせても正しいSpecificationを返却する 値が存在しない場合")
        @Test
        void equalTaskIdAndEqualUserIdOfEmpty() {
            Optional<Task> actualOpt = taskRepository.findOne(
                    Specification.where(
                            TaskSpecificationHelper.fetchUser().and(
                                    TaskSpecificationHelper.equalTaskId(1L).and(
                                            TaskSpecificationHelper.equalAppUserId(1L)
                                    )
                            )

                    )
            );

            assertTrue(actualOpt.isEmpty());


        }


    }
}