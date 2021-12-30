package com.example.my_todo_application.task_management.domain.model;

import lombok.Getter;

public enum Importance {
    VERY_HIGH("極高"), HIGH("高い"), NORMAL("普通"), LOW("低い"), VERY_LOW("極低");

    @Getter
    private final String viewName;

    Importance(String viewName) {
        this.viewName = viewName;
    }
}
