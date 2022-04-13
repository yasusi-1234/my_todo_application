// 

// [
//     {taskId: 1, taskName: 'タスク1', startDate: '2010/01/01', endDate: '2010/01/10',
//   detail: 'This is sample task1.', progress: 50, notice: true },
//     {taskId: 2, taskName: 'タスク2', startDate: '2010/01/01', endDate: '2010/01/15',
//   detail: 'This is sample task2.', progress: 50, notice: true },
//     {taskId: 3, taskName: 'タスク3', startDate: '2010/01/16', endDate: '2010/01/18',
//   detail: 'This is sample task1.', progress: 50, notice: true }
// ];

// <div class="task-bar">
//     <div class="task-bar-box">
//     <div class="task-bar-name">タスク1: 55%</div>
//     <div class="task-bar-progress"></div>
//     </div>
// </div>

// taskbarの位置 進捗度 日数 taskName
/**
 * タスク情報を元にタスクグラフに使うタスクバーのオブジェクトをリスト型のオブジェクト形式にして返却
 * index, progress, taskDays, taskName, startDate, endDateをリストに含んだデータ
 * @param {*} tasks 
 * @returns 
 */
function createTaskBars(tasks) {
    const taskSize = tasks.length;
    let taskBars = [];
    if(taskSize === 0){
        // タスクが無い場合
        return taskBars;
    } else if(taskSize < 7) {
        // タスクが7個未満の時
        for(let i = 0; i < taskSize; i++ ){
            const taskDays = getDiffDays(tasks[i].startDate, tasks[i].endDate);
            taskBars.push({
                taskId: tasks[i].taskId,
                taskName: tasks[i].taskName,
                startDate: tasks[i].startDate,
                endDate: tasks[i].endDate,
                detail: tasks[i].detail,
                importance: tasks[i].importance,
                progress: tasks[i].progress,
                notice: tasks[i].notice,
                index: i + 1,
                taskDays: taskDays + 1,
                isActive: false
            });
        }

        return taskBars;
    } else {
        // タスクが7個以上の時
        let taskMaxIndex = 2;
        for(let i = 0; i < taskSize; i++ ) {
            let taskIndex = 1;
            
            if(taskBars.length >= 1){
                for(let j = 1; j <= taskMaxIndex; j++ ){
                    const taskBarsOfIndex = taskBars.filter(taskBar => taskBar.index === j);
                    const dateCrossed = taskBarsOfIndex.some(taskBar => isDateCrossed(taskBar, tasks[i]));

                    if(dateCrossed){
                        taskIndex++;
                    } else {
                        taskMaxIndex = j === taskMaxIndex ? taskMaxIndex + 1 : taskMaxIndex; 
                        break;
                    }
                }
            }

            const taskDays = getDiffDays(tasks[i].startDate, tasks[i].endDate);

            taskBars.push({
                taskId: tasks[i].taskId,
                taskName: tasks[i].taskName,
                startDate: tasks[i].startDate,
                endDate: tasks[i].endDate,
                detail: tasks[i].detail,
                importance: tasks[i].importance,
                progress: tasks[i].progress,
                notice: tasks[i].notice,
                index: taskIndex,
                taskDays: taskDays + 1,
                isActive: false
                // 重要度を追加
            })
        }

        return taskBars;
    }
}

/**
 * 引数で与えられた開始日と終了日を持つ要素同士の日付が交差しているかチェックするメソッド
 * @param {*} task1 
 * @param {*} task2 
 * @returns 
 */
function isDateCrossed(task1, task2) {
    const startDate1 = new Date(task1.startDate);
    const endDate1 = new Date(task1.endDate);
    const startDate2 = new Date(task2.startDate);
    const endDate2 = new Date(task2.endDate);

    return (startDate2 <= startDate1 && startDate1 <= endDate2) ||
    (startDate2 <= endDate1 && endDate1 <= endDate2) ||
    (startDate1 <= startDate2 && startDate2 <= endDate1) ||
    (startDate1 <= endDate2 && endDate2 <= endDate1);
}