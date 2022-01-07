package com.example.my_todo_application.task_management.controller.form;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UserRegisterFormクラスのバリデーションテスト")
@SpringBootTest
class UserRegisterFormTest {

    @Autowired
    Validator validator;

    @DisplayName("正常系のテスト")
    @Test
    void userRegisterFromOfNoProblem() {
        // setup
        TaskRegisterForm form = testTaskRegisterForm();
        BindingResult bindingResult = new BindException(form, "userRegisterForm");
        // action
        validator.validate(form, bindingResult);
        // check
        assertFalse(bindingResult.hasErrors());
    }

    @DisplayName("firstNameのテスト")
    @Nested
    class FirstName {
        @Test
        @DisplayName("firstNameが空の場合エラーになる")
        void firstNameIsEmpty() {
            // setup
            UserRegisterForm form = testTaskRegisterForm();
            form.setFirstName("");
            BindingResult bindingResult = new BindException(form, "userRegisterForm");

            validator.validate(form, bindingResult);

            assertTrue(bindingResult.hasFieldErrors("firstName"));
            System.out.println(bindingResult.getFieldError("firstName").getDefaultMessage());
        }

        @Test
        @DisplayName("firstNameがnullの場合エラーになる")
        void firstNameIsNull() {
            // setup
            UserRegisterForm form = testTaskRegisterForm();
            form.setFirstName(null);
            BindingResult bindingResult = new BindException(form, "userRegisterForm");

            validator.validate(form, bindingResult);

            assertTrue(bindingResult.hasFieldErrors("firstName"));
            System.out.println(bindingResult.getFieldError("firstName").getDefaultMessage());
        }
    }
    
    @DisplayName("lastNameのテスト")
    @Nested
    class LastName {
        @Test
        @DisplayName("lastNameが空の場合エラーになる")
        void lastNameIsEmpty() {
            // setup
            UserRegisterForm form = testTaskRegisterForm();
            form.setLastName("");
            BindingResult bindingResult = new BindException(form, "userRegisterForm");

            validator.validate(form, bindingResult);

            assertTrue(bindingResult.hasFieldErrors("lastName"));
            System.out.println(bindingResult.getFieldError("lastName").getDefaultMessage());
        }

        @Test
        @DisplayName("lastNameがnullの場合エラーになる")
        void lastNameIsNull() {
            // setup
            UserRegisterForm form = testTaskRegisterForm();
            form.setLastName(null);
            BindingResult bindingResult = new BindException(form, "userRegisterForm");

            validator.validate(form, bindingResult);

            assertTrue(bindingResult.hasFieldErrors("lastName"));
            System.out.println(bindingResult.getFieldError("lastName").getDefaultMessage());
        }
    }

    private TaskRegisterForm testTaskRegisterForm() {
        TaskRegisterForm form = new TaskRegisterForm();
        form.setFirstName("test");
        form.setLastName("test");
        form.setMailAddress("test@xxx.xx.xx");
        form.setPassword("password");
        return form;
    }
}