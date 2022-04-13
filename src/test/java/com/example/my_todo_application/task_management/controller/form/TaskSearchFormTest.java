package com.example.my_todo_application.task_management.controller.form;

import com.example.my_todo_application.task_management.domain.model.Importance;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ValidationAutoConfiguration.class)
@DisplayName("TaskRegisterFormクラスのバリデーションテスト")
class TaskSearchFormTest {

    @Autowired
    Validator validator;

    @DisplayName("正常に処理されるTaskSearchForm")
    @Test
    void normalFormTest() {
        TaskSearchForm taskSearchForm = makeTaskSearchFormForTest();
        BindingResult bindingResult = new BindException(taskSearchForm, "taskSearchForm");

        validator.validate(taskSearchForm, bindingResult);

        assertFalse(bindingResult.hasErrors());
    }

    @DisplayName("日付のエラーチェック")
    @Nested
    class DateFieldTest {

        @Test
        @DisplayName("fromDateフィールドの日付がtoDateフィールドよりも前の日付が設定されていたら、異常値として処理される")
        void dateFieldAbnormalTest() {
            TaskSearchForm taskSearchForm = makeTaskSearchFormForTest();
            // setup
            taskSearchForm.setFromDate(LocalDate.of(2010, 1,10));
            taskSearchForm.setToDate(LocalDate.of(2010, 1,9));

            BindingResult bindingResult = new BindException(taskSearchForm, "taskSearchForm");

            validator.validate(taskSearchForm, bindingResult);

            assertTrue(bindingResult.hasErrors());

        }

        @Test
        @DisplayName("fromDateフィールドの日付がnullの場合は正常値として処理される")
        void fromDateNullTest() {
            TaskSearchForm taskSearchForm = makeTaskSearchFormForTest();
            // setup
            taskSearchForm.setFromDate(null);

            BindingResult bindingResult = new BindException(taskSearchForm, "taskSearchForm");

            validator.validate(taskSearchForm, bindingResult);

            assertFalse(bindingResult.hasErrors());
        }

        @Test
        @DisplayName("toDateフィールドの日付がnullの場合は正常値として処理される")
        void toDateNullTest() {
            TaskSearchForm taskSearchForm = makeTaskSearchFormForTest();
            // setup
            taskSearchForm.setToDate(null);

            BindingResult bindingResult = new BindException(taskSearchForm, "taskSearchForm");

            validator.validate(taskSearchForm, bindingResult);

            assertFalse(bindingResult.hasErrors());
        }
    }

    /** テスト用のタスク検索フォーム */
    private TaskSearchForm makeTaskSearchFormForTest() {
        TaskSearchForm form = new TaskSearchForm();
        form.setTaskName("test");
        form.setFromDate(LocalDate.of(2010, 1, 1));
        form.setToDate(LocalDate.of(2010, 1, 2));
        form.setDetail("this is test");
        form.setProgress(Progress.ALL);
        form.setImportanceList(Arrays.asList(Importance.values()));

        return form;
    }

}