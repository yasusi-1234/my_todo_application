package com.example.my_todo_application.task_management.controller;

import com.example.my_todo_application.security.model.AppUserDetails;
import com.example.my_todo_application.task_management.controller.form.Progress;
import com.example.my_todo_application.task_management.controller.form.TaskRegisterForm;
import com.example.my_todo_application.task_management.controller.form.TaskSearchForm;
import com.example.my_todo_application.task_management.domain.model.AppUser;
import com.example.my_todo_application.task_management.domain.model.Importance;
import com.example.my_todo_application.task_management.domain.model.Task;
import com.example.my_todo_application.task_management.domain.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Controller
@RequestMapping("task")
@RequiredArgsConstructor
@SessionAttributes(types = { TaskSearchForm.class })
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

    @ModelAttribute("taskSearchForm")
    public TaskSearchForm taskSearchForm() {
        return new TaskSearchForm();
    }

    @GetMapping("home")
    public String getTaskHome(
            @AuthenticationPrincipal AppUserDetails user,
            @ModelAttribute("taskSearchForm") TaskSearchForm taskSearchForm,
            @ModelAttribute("taskRegisterForm") TaskRegisterForm taskRegisterForm,
            Model model) {
        List<Task> userTasks = taskService.findTasksOf(
                user.getAppUser().getAppUserId(),
                taskSearchForm.getSearchTaskName(),
                taskSearchForm.getFromDate(),
                taskSearchForm.getToDate(),
                taskSearchForm.getImportanceList(),
                taskSearchForm.getSearchProgress());

        model.addAttribute("userTasks", userTasks);
        model.addAttribute("username", user.getAppUser().getLastName());
        return "task/task-home";
    }

    @PostMapping("register")
    public String postRegisterTask(
            @AuthenticationPrincipal AppUserDetails user,
            @Validated @ModelAttribute("taskRegisterForm") TaskRegisterForm taskRegisterForm,
            BindingResult bindingResult,
            @ModelAttribute("taskSearchForm") TaskSearchForm taskSearchForm,
            UriComponentsBuilder uriComponentsBuilder,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return getTaskHome(user, taskSearchForm, taskRegisterForm, model);
        }

        Task saveTask = makeTaskFromForm(taskRegisterForm, user.getAppUser());
        taskService.saveTask(saveTask);

        String uri = MvcUriComponentsBuilder.relativeTo(uriComponentsBuilder)
                .withMappingName("TC#getTaskHome").build();

        redirectAttributes.addFlashAttribute("complete", "新規タスクを登録しました");

        return "redirect:" + uri;
    }

    @PostMapping("delete")
    public String postDeleteTask(
            @AuthenticationPrincipal AppUserDetails user,
            @RequestParam("taskId") Long taskId,
            UriComponentsBuilder uriComponentsBuilder,
            RedirectAttributes redirectAttributes) {

        taskService.deleteTaskIdAndUserId(taskId, user.getAppUser().getAppUserId());

        redirectAttributes.addFlashAttribute("complete", "タスクの削除が完了しました");

        String uri = MvcUriComponentsBuilder.relativeTo(uriComponentsBuilder)
                .withMappingName("TC#getTaskHome").build();

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
        task.setStartDate(form.getStartDate());
        task.setEndDate(form.getEndDate());
        task.setDetail(form.getDetail());
        task.setImportance(form.getImportance());
        task.setNotice(form.isNotice());
        task.setProgress(form.getProgress());

        task.setAppUser(appUser);
        return task;
    }
}
