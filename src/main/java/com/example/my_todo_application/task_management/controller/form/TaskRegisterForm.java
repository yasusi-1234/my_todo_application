package com.example.my_todo_application.task_management.controller.form;

import com.example.my_todo_application.task_management.controller.validator.DateBetween;
import com.example.my_todo_application.task_management.domain.model.Importance;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * Task登録用のFormムクラス
 */
@Data
@DateBetween(startField = "startDate", endField = "endDate")
public class TaskRegisterForm {

    /** タスクID */
    private Long taskId;
    /** タスク名 */
    @NotBlank
    private String taskName;
    /** タスク開始時刻 */
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate startDate;
    /** タスク終了時刻 */
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate endDate;
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
