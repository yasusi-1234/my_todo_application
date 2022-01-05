package com.example.my_todo_application.task_management.domain.service.specification;

import com.example.my_todo_application.task_management.controller.form.Progress;
import com.example.my_todo_application.task_management.domain.model.Importance;
import com.example.my_todo_application.task_management.domain.model.Task;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * TaskRepositoryのSpecificationを生成するためのHelperクラス
 */
public final class TaskSpecificationHelper {

    /**
     * N+1問題回避用のメソッド
     *
     * @return {@link Specification} N+1問題を解決するためのSpecification
     */
    public static Specification<Task> fetchUser() {
        return (root, query, cb) -> {
            if (query.getResultType() == Long.class || query.getResultType() == long.class) {
                root.join("appUser");
            } else {
                root.fetch("appUser");
            }
            return query.getRestriction();
        };
    }

    /**
     * 引数で指定されたタスク名を含むタスクを返却するSpecificationを生成する
     * @param taskName タスク名
     * @return タスク名を含むタスクを返却するSpecification
     */
    public static Specification<Task> likeTaskName(String taskName) {
        return StringUtils.hasText(taskName) ? (root, query, cb) -> cb.like(root.get("taskName"), "%" + taskName + "%")
        : null;
    }

    /**
     * @param id {@link com.example.my_todo_application.task_management.domain.model.AppUser}
     *           のユーザーID
     * @return ユーザーのIDが同一の物を検索するためのSpecification
     */
    public static Specification<Task> equalAppUserId(Long id) {
        return id == null || id < 0L ? null
                : (root, query, cb) -> cb.equal(root.get("appUser").get("appUserId"), id);
    }

    /**
     * {@link com.example.my_todo_application.task_management.domain.model.AppUser} のstartDatetime
     * {@link com.example.my_todo_application.task_management.domain.model.AppUser} のendDatetime
     * の何れかが引数に指定されているstartとendの間に含まれている情報を返却するためのSpecification
     *
     * @param start 検索したい範囲の開始日時情報
     * @param end   検索したい範囲の終了日時情報
     * @return 引数に指定されているstartとendの間をAppUserのstartDatetimeかendDateTimeを満たすSpecification
     */
    public static Specification<Task> betweenDatetime(LocalDateTime start, LocalDateTime end) {
        return start == null || end == null || start.isAfter(end) ? null :
                (root, query, cb) -> cb.or(cb.between(root.get("startDatetime"), start, end),
                        cb.between(root.get("endDatetime"), start, end));
    }

    /**
     * {@link Task} のimportanceが一致するものを検索するためのSpecificationを返却する
     * @param importance 重要度
     * @return Taskのimportanceが一致する物を検索するためのSpecification
     */
    public static Specification<Task> equalImportance(Importance importance){
        return importance == null ? null :
                (root, query, cb) -> cb.equal(root.get("importance"), importance);
    }

    /**
     * progress(進捗度)が100のTaskを抽出するためのSpecificationを返却する
     * @return progress(進捗度)が100のTaskを抽出するためのSpecification
     */
    public static Specification<Task> progress(Progress progress){
        switch (progress){
            case REGISTER:
                return (root, query, cb) ->
                        cb.equal(root.get("progress"), 100);
            case WORKING:
                return (root, query, cb) ->
                        cb.between(root.get("progress"), 1, 99);
            case NOT_START:
                return (root, query, cb) ->
                        cb.equal(root.get("progress"), 0);
        }
        return null;
    }

}
