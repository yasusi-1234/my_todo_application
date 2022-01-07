package com.example.my_todo_application.task_management.controller.form;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UserRegisterForm {

    /** 名前 */
    @NotBlank
    private String firstName;
    /** 苗字 */
    @NotBlank
    private String lastName;
    /** メールアドレス */
    @Email
    @NotBlank
    private String mailAddress;
    /** パスワード */
    @Size(min = 8)
    @NotBlank
    private String password;
}
