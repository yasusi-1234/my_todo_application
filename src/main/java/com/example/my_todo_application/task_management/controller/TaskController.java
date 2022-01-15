package com.example.my_todo_application.task_management.controller;

import com.example.my_todo_application.task_management.controller.form.Progress;
import com.example.my_todo_application.task_management.controller.form.TaskRegisterForm;
import com.example.my_todo_application.task_management.controller.form.TaskSearchForm;
import com.example.my_todo_application.task_management.domain.model.AppUser;
import com.example.my_todo_application.task_management.domain.model.Importance;
import com.example.my_todo_application.task_management.domain.model.Task;
import com.example.my_todo_application.task_management.domain.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
@RequestMapping("task")
@RequiredArgsConstructor
public class TaskController {


    private final TaskService taskService;

    @ModelAttribute("progress")
    public Progress[] progresses() {
        return Progress.values();
    }

    @ModelAttribute("importance")
    public Importance[] importance() {
        return Importance.values();
    }

    @GetMapping("home")
    public String getTaskHome(
            @ModelAttribute("taskSearchForm") TaskSearchForm taskSearchForm,
            @ModelAttribute("TaskRegisterFrom") TaskRegisterForm taskRegisterForm,
            Model model) {

        return "task/task-home";
    }

    @PostMapping("register")
    public String postRegisterTask(
            @Validated @ModelAttribute("taskRegisterForm") TaskRegisterForm taskRegisterForm,
            @ModelAttribute("taskSearchForm") TaskSearchForm taskSearchForm,
            BindingResult bindingResult,
            UriComponentsBuilder uriComponentsBuilder,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "task/task-home";
        }

        // ------- セキュリティーを入れてからユーザー情報を挿入 ---------
        Task saveTask = makeTaskFromForm(taskRegisterForm, null);
        taskService.saveTask(saveTask);

        String uri = MvcUriComponentsBuilder.relativeTo(uriComponentsBuilder)
                .withMethodName(this.getClass(), "getTaskHome").build().toString();

        redirectAttributes.addFlashAttribute("complete", "新規タスクを登録しました");

        return "redirect:" + uri;
    }

    @PostMapping("delete")
    public String postDeleteTask(
            @RequestParam("taskId") Long taskId,
            UriComponentsBuilder uriComponentsBuilder,
            RedirectAttributes redirectAttributes) {
        taskService.deleteByTaskId(taskId);

        redirectAttributes.addFlashAttribute("complete", "タスクの削除が完了しました");

        String uri = MvcUriComponentsBuilder.relativeTo(uriComponentsBuilder)
                .withMethodName(this.getClass(), "getTaskHome").build().toString();

        return "redirect:" + uri;
    }


    /**
     * タスクフォームとユーザー情報からタスクオブジェクトを生成し返却する
     *
     * @param form    タスク登録フォーム
     * @param appUser アプリケーションユーザー
     * @return フォームとユーザー情報から作成されたタスクオブジェクト
     */
    private Task makeTaskFromForm(TaskRegisterForm form, AppUser appUser) {
        Task task = new Task();
        task.setTaskId(form.getTaskId());
        task.setTaskName(form.getTaskName());
        task.setStartDatetime(form.getStartDateTime());
        task.setEndDatetime(form.getEndDateTime());
        task.setDetail(form.getDetail());
        task.setImportance(form.getImportance());
        task.setNotice(form.isNotice());
        task.setProgress(form.getProgress());

        task.setAppUser(appUser);
        return task;
    }
}
