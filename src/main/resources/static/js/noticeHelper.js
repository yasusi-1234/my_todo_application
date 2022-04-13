class NoticeHelper {
    constructor(tasks) {
        this.now = this._createDateNow();
        // this.now = new Date('2010-01-06')
        this.noticeInformation = this._createNoticeInfo(tasks);
    }

    _createNoticeInfo(tasks) {
        return tasks.filter(task => 
            task.notice && task.progress !== 100 && this._duringTheTask(task)
        ).map(task => {
                const taskDays = this._getDays(new Date(task.startDate), new Date(task.endDate));
                const nowDays = this._getDays(new Date(task.startDate), this.now);
                const noticeMessageObj = this._getNoticeMessage(taskDays, nowDays, task.progress);

                return {taskName: task.taskName, late: noticeMessageObj.late, message: noticeMessageObj.message };
        });
    }

    /** 今日の日付を取得する */
    _createDateNow() {
        const nowDate = new Date();
        return new Date(nowDate.getFullYear(), nowDate.getMonth(), nowDate.getDate());
    }

    /** 今日がタスクのstartDate, endDateの間かを論理値で返却する */
    _duringTheTask(task) {
        const startDate = new Date(task.startDate);
        const endDate = new Date(task.endDate);

        return startDate <= this.now && this.now < endDate;
    }

    /** 受け取った日付の日数を返却する */
    _getDays(startDate, endDate) {
        return (endDate - startDate) / 1000 / 60 / 60 / 24 + 1;
    }

    /** タスクの期待した進捗度より高いか低いかを計算し、結果を返却する */
    _getNoticeMessage(taskDaysCount, nowDaysCount, progress) {
        const expectedProgress = Math.floor(nowDaysCount / taskDaysCount * 100);
        if (expectedProgress > progress) {
            return {
                late: true,
                message: `${expectedProgress - progress}% 分の進捗遅延があります`
            }
        } else {
            return {
                late: false,
                message: `${progress - expectedProgress}% 分の進捗に余裕があります`
            }
        }
    }
}

const noticeObjects = new NoticeHelper(sample);