package com.example.my_todo_application.task_management.domain.service;

import com.example.my_todo_application.task_management.controller.form.Progress;
import com.example.my_todo_application.task_management.domain.model.Importance;
import com.example.my_todo_application.task_management.domain.model.Task;
import com.example.my_todo_application.task_management.domain.repository.TaskRepository;
import com.example.my_todo_application.task_management.domain.service.specification.TaskSpecificationHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService{

    @Autowired
    private final TaskRepository taskRepository;

    /**
     * 指定されたタスクIDのタスクを1件返却する。もし存在しないタスクIDが指定された場合はNullを返却する
     * @param taskId タスクID
     * @return 指定されたタスクIDのタスク
     */
    @Override
    public Task findByTaskId(Long taskId){
        return taskRepository.findById(taskId).orElse(null);
    }

    /**
     * 指定されたユーザーIDのタスクを全て検索し返却する
     * @param userId ユーザーID
     * @return 指定されたユーザーIDの全てのタスク
     */
    @Override
    public List<Task> findAllTaskByUserId(Long userId){
        return taskRepository.findAll(Specification.where(
                TaskSpecificationHelper.fetchUser().and(
                        TaskSpecificationHelper.equalAppUserId(userId)
                )
        ));
    }

    /**
     * 指定されたタスクを1件DBに保存する
     * @param task タスク情報
     * @return 保存されたタスク情報
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Task saveTask(Task task){
        return taskRepository.save(task);
    }

    /**
     * 指定されたタスクIDを持つタスクを1件リポジトリから削除する
     * @param taskId タスクID
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void deleteByTaskId(Long taskId){
        taskRepository.deleteById(taskId);
    }

    /**
     * タスク名・開始時刻・終了時刻・重要度・タスクの進捗度の情報を元に検索されたタスクリスト情報を返却する
     * @param taskName タスク名
     * @param startTime 開始日時・時刻情報
     * @param endTime 終了日時・時刻情報
     * @param importance 重要度
     * @param progress タスクの進捗情報
     * @return 引数で指定された情報のタスクリスト
     */
    @Override
    public List<Task> findTasksOf(
            String taskName,
            LocalDateTime startTime,
            LocalDateTime endTime,
            Importance importance,
            Progress progress){
        return taskRepository.findAll(Specification.where(TaskSpecificationHelper.fetchUser()
                .and(
                        TaskSpecificationHelper.likeTaskName(taskName).and(
                                TaskSpecificationHelper.betweenDatetime(startTime, endTime).and(
                                        TaskSpecificationHelper.equalImportance(importance).and(
                                                TaskSpecificationHelper.progress(progress)
                                        )
                                )
                        )
                )
        ));
    }


}
