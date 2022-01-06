package com.example.my_todo_application.task_management.controller.form;

import com.example.my_todo_application.task_management.domain.model.Importance;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class TaskRegisterForm {
    /** タスク名 */
    @NotBlank
    private String taskName;
    /** タスク開始時刻 */
    @DateTimeFormat(pattern = "yyyy/MM/dd'T'HH:mm:ss")
    private LocalDateTime startDateTime;
    /** タスク終了時刻 */
    @DateTimeFormat(pattern = "yyyy/MM/dd'T'HH:mm:ss")
    private LocalDateTime endDateTime;
    /** タスクの詳細 */
    private String detail;
    /** タスクの重要度 */
    @NotNull
    private Importance importance;
    /** 通知機能 */
    @NotNull
    private boolean notice;
    /** タスク進捗度 */
    @Min(value = 0)
    @Max(value = 100)
    private int progress;
}
