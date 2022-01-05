package com.example.my_todo_application.task_management.controller.form;

import lombok.Getter;

public enum Progress {

    REGISTER("完了"),
    WORKING("作業中"),
    NOT_START("未着手");

    @Getter
    private final String viewName;

    Progress(String viewName) {
        this.viewName = viewName;
    }
}
