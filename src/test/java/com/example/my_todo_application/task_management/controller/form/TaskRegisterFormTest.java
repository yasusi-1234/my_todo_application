package com.example.my_todo_application.task_management.controller.form;

import com.example.my_todo_application.task_management.domain.model.Importance;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

@SpringBootTest(classes = ValidationAutoConfiguration.class)
@DisplayName("TaskRegisterFormクラスのバリデーションテスト")
class TaskRegisterFormTest {

    @Autowired
    Validator validator;

    @Test
    @DisplayName("正常系")
    void normalTest() {
        // setup
        TaskRegisterForm taskRegisterForm = createNormalTaskRegisterForm();
        BindingResult bindingResult = new BindException(taskRegisterForm, "taskRegisterForm");
        // テスト実施
        validator.validate(taskRegisterForm, bindingResult);

        assertFalse(bindingResult.hasErrors());
    }

    @ParameterizedTest
    @MethodSource("abNormalTaskNames")
    @DisplayName("taskNameの値が空かnullの場合はerrorになる")
    void taskNameTest(String value){
        // setup
        TaskRegisterForm taskRegisterForm = createNormalTaskRegisterForm();
        taskRegisterForm.setTaskName(value);
        BindingResult bindingResult = new BindException(taskRegisterForm, "taskRegisterForm");
        // テスト実施
        validator.validate(taskRegisterForm, bindingResult);

        assertTrue(bindingResult.hasFieldErrors("taskName"));
    }

    @Test
    @DisplayName("importanceが指定されていない場合はErrorになる")
    void importanceTest() {
        // setup
        TaskRegisterForm taskRegisterForm = createNormalTaskRegisterForm();
        taskRegisterForm.setImportance(null);
        BindingResult bindingResult = new BindException(taskRegisterForm, "taskRegisterForm");
        // テスト実施
        validator.validate(taskRegisterForm, bindingResult);

        assertTrue(bindingResult.hasFieldErrors("importance"));
    }

    @DisplayName("progressのテスト")
    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 50, 100, 101})
    void progressTest(int value) {
        // setup
        TaskRegisterForm taskRegisterForm = createNormalTaskRegisterForm();
        taskRegisterForm.setProgress(value);
        BindingResult bindingResult = new BindException(taskRegisterForm, "taskRegisterForm");
        // テスト実施
        validator.validate(taskRegisterForm, bindingResult);
        // 値が0未満或いは101以上の場合
        assumingThat(value < 0 || value > 100,
                () -> assertTrue(bindingResult.hasFieldErrors("progress")));
        // 値が0以上100以下の場合
        assumingThat(value >= 0 && value <= 100,
                () -> assertFalse(bindingResult.hasFieldErrors("progress")));
    }

    @DisplayName("日付関連のテスト")
    @Nested
    class StartAndEndDateTimeTest {

        @DisplayName("正常系のテスト")
        @Test
        void normalStartAndEndTime() {
            // setup
            TaskRegisterForm form = createNormalTaskRegisterForm();
            form.setStartDateTime(LocalDateTime.of(2010, 1, 1, 0, 0, 0));
            form.setEndDateTime(LocalDateTime.of(2010, 1, 2, 0, 0, 0));
            BindingResult bindingResult = new BindException(form, "taskRegisterFrom");
            // テスト実施
            validator.validate(form, bindingResult);
            assertFalse(bindingResult.hasFieldErrors("startDateTime"));
        }

        @DisplayName("日付の開始時刻より終了時刻が前の場合エラーになる")
        @Test
        void beforeEndTimeOfStartTime() {
            // setup
            TaskRegisterForm form = createNormalTaskRegisterForm();
            form.setStartDateTime(LocalDateTime.of(2010, 2, 1, 0, 0, 0));
            form.setEndDateTime(LocalDateTime.of(2010, 1, 1, 0, 0, 0));
            BindingResult bindingResult = new BindException(form, "taskRegisterFrom");
            // テスト実施
            validator.validate(form, bindingResult);
            assertFalse(bindingResult.hasFieldErrors("startDateTime"));
        }

        @DisplayName("日付の開始時刻と終了時刻が同じの場合エラーになる")
        @Test
        void sameEndTimeOfStartTime() {
            // setup
            TaskRegisterForm form = createNormalTaskRegisterForm();
            form.setStartDateTime(LocalDateTime.of(2010, 1, 1, 0, 0, 0));
            form.setEndDateTime(LocalDateTime.of(2010, 1, 1, 0, 0, 0));
            BindingResult bindingResult = new BindException(form, "taskRegisterFrom");
            // テスト実施
            validator.validate(form, bindingResult);
            assertFalse(bindingResult.hasFieldErrors("startDateTime"));
        }
    }

    private static Stream<String> abNormalTaskNames() {
        return Stream.of("", null);
    }

    private TaskRegisterForm createNormalTaskRegisterForm() {
        TaskRegisterForm taskRegisterForm = new TaskRegisterForm();
        taskRegisterForm.setTaskName("test");
        taskRegisterForm.setStartDateTime(LocalDateTime.of(2010, 1, 1, 0, 0, 0));
        taskRegisterForm.setEndDateTime(LocalDateTime.of(2010, 1, 20, 1, 2, 3));
        taskRegisterForm.setDetail("test");
        taskRegisterForm.setImportance(Importance.NORMAL);
        taskRegisterForm.setNotice(true);
        taskRegisterForm.setProgress(50);
        return taskRegisterForm;
    }
}