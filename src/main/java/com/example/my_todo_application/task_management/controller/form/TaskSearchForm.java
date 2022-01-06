package com.example.my_todo_application.task_management.controller.form;

import com.example.my_todo_application.task_management.domain.model.Importance;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class TaskSearchForm {

    /** タスク名 */
    private String taskName;
    /** 検索したい開始時刻 */
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fromDate;
    /** 検索したい終了時刻 */
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime toDate;
    /** タスク重要度 */
    private Importance importance;
    /** タスク進捗状況 */
    private Progress progress;

}
