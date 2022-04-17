package com.example.my_todo_application.task_management.domain.service.specification;

import com.example.my_todo_application.task_management.controller.form.Progress;
import com.example.my_todo_application.task_management.domain.model.Importance;
import com.example.my_todo_application.task_management.domain.model.Task;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.example.my_todo_application.task_management.common.EntityNames.*;

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
                root.join(APP_USER).join(ROLE);
            } else {
                root.fetch(APP_USER).fetch(ROLE);
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
        return StringUtils.hasText(taskName) ? (root, query, cb) -> cb.like(root.get(TASK_NAME), "%" + taskName + "%")
        : null;
    }

    /**
     * @param id {@link com.example.my_todo_application.task_management.domain.model.AppUser}
     *           のユーザーID
     * @return ユーザーのIDが同一の物を検索するためのSpecification
     */
    public static Specification<Task> equalAppUserId(Long id) {
        return id == null || id < 0L ? null
                : (root, query, cb) -> cb.equal(root.get(APP_USER).get(APP_USER_ID), id);
    }

    /**
     * 引数で指定された日付以降の終了日を持つ {@link Task} を抽出するためのSpecificationを返却する
     *
     * @param start 検索したい先端の日付データ
     * @return 引数で指定された日付以降の終了日を持つ {@link Task} を抽出するためのSpecification
     */
    public static Specification<Task> afterDate(LocalDate start) {
        return start == null ? null :
                (root, query, cb) -> cb.greaterThanOrEqualTo(root.get(END_DATE), start);
    }

    /**
     * 引数で指定された日付以前の開始日を持つ {@link Task} を抽出するためのSpecificationを返却する
     *
     * @param end 検索したい末端の日付データ
     * @return 引数で指定された日付以前の開始日を持つ {@link Task} を抽出するためのSpecification
     */
    public static Specification<Task> beforeDate(LocalDate end) {
        return end == null ? null :
                (root, query, cb) -> cb.lessThanOrEqualTo(root.get(START_DATE), end);
    }

    /**
     * {@link Task} のimportanceが一致するものを検索するためのSpecificationを返却する
     * @param importance 重要度
     * @return Taskのimportanceが一致する物を検索するためのSpecification
     */
    public static Specification<Task> equalImportance(Importance importance){
        return importance == null ? null :
                (root, query, cb) -> cb.equal(root.get(IMPORTANCE), importance);
    }

    /**
     * {@link Task} のimportanceと一致するものを抽出するためのSpecificationを返却する
     * @param importance 重要度のリスト
     * @return Taskのimportanceが一致するものを研削するためのSpecification
     */
    public static Specification<Task> inImportance(List<Importance> importance){
        return CollectionUtils.isEmpty(importance) ? null :
                (root, query, cb) -> root.get(IMPORTANCE).in(importance);
    }

    /**
     * {@link Task} のnoticeと一致するものを検索するためのSpecificationを返却する
     * @param notice 通知機能
     * @return 通知機能が指定された引数を返却するSpecification
     */
    public static Specification<Task> inNotice(Boolean notice) {

        return notice == null ? null :
                (root, query, cb) -> cb.equal(root.get(NOTICE), notice);
    }

    /**
     * {@link Task} のstartDatetimeとendDatetimeの間に一致するタスクを検索するためのSpecificationを返却する
     * @param targetDateTime 指定の日付
     * @return {@link Task} のstartDatetimeとendDatetimeの間に一致するタスクを検索するためのSpecification
     */
    public static Specification<Task> inTaskDateTime(LocalDateTime targetDateTime) {
        return targetDateTime == null ? null :
                (root, query, cb) -> cb.and(cb.greaterThanOrEqualTo(root.get(START_DATE), targetDateTime),
                        cb.lessThanOrEqualTo(root.get(END_DATE), targetDateTime));
    }

    /**
     * progress(進捗度)が100(完了)のTask、99以下(未完了)のTaskを抽出するためのSpecificationを返却する
     * @return progress(進捗度)が100(完了)のTask、99以下(未完了)のTaskを抽出するためのSpecification
     */
    public static Specification<Task> progress(Progress progress){

        final int TASK_REGISTER = 100;

        if (progress == null){
            return null;
        }

        switch (progress){
            case REGISTER:
                return (root, query, cb) ->
                        cb.equal(root.get(PROGRESS), TASK_REGISTER);
            case NOT_REGISTER:
                return (root, query, cb) ->
                        cb.notEqual(root.get(PROGRESS), TASK_REGISTER);
            case ALL:
            default:
                return null;

        }
    }

    /**
     * {@link Task} のtaskIdと一致する情報を返却するSpecificationメソッド
     * @param taskId タスクID
     * @return {@link Task} のtaskIdと一致する情報を返却するSpecification
     */
    public static Specification<Task> equalTaskId(Long taskId) {
        return taskId == null ? null
                : (root, query, cb) -> cb.equal(root.get(TASK_ID), taskId);

    }
}
