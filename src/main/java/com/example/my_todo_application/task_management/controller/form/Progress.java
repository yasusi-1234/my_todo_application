package com.example.my_todo_application.task_management.controller.form;

import lombok.Getter;

public enum Progress {

    REGISTER("完了"),
    NOT_REGISTER("未完了"),
    ALL("全て");

    @Getter
    private final String viewName;

    Progress(String viewName) {
        this.viewName = viewName;
    }
}
