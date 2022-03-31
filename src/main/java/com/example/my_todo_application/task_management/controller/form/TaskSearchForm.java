package com.example.my_todo_application.task_management.controller.form;

import com.example.my_todo_application.task_management.controller.validator.DateBetween;
import com.example.my_todo_application.task_management.controller.validator.FormAction;
import com.example.my_todo_application.task_management.domain.model.Importance;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
@DateBetween(startField = "fromDate", endField = "toDate", formType = FormAction.READ)
public class TaskSearchForm {

    /** タスク名 */
    private String taskName;
    /** 検索したい開始時刻 */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fromDate;
    /** 検索したい終了時刻 */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate toDate;
    /** タスク詳細 */
    private String detail;
    /** タスク重要度 */
    private List<Importance> importanceList;
    /** タスク進捗状況 */
    private Progress progress;

}
