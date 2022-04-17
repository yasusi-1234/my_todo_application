const importanceValues = {VERY_HIGH: '極高', HIGH: '高い', NORMAL: '普通', LOW: '低い', VERY_LOW: '極低'};


Vue.component('calendar-head', {
    props: ['month'],
    template: 
    `<div class="task-calendar-head">
        <div class="year-month" :style="{width: (25 * month.length)  + 'px' }" ref="year_month">{{ month[0].year }}年{{ month[0].month }}月</div>
        <div class="days">
            <div class="day" v-for="(day, dayIndex) in month" :key="dayIndex" :class="{ sunday: day.dayOfWeek === '日', saturday: day.dayOfWeek === '土' }">
                <div class="day-inner" ref="day_inner">
                    <div>{{ day.day }}</div>
                    <div class="day-of-week">{{ day.dayOfWeek }}</div>
                </div>
            </div>
        </div>
    </div>`,
    methods: {
        // 親から呼ばれるメソッドのテスト用
        childMethod() {
            console.log('this is child obj. call now.')
        }
    }
});

Vue.component('task-bars', {
    props: ['taskbars'],
    template: `
    <div>
        <div class="task-bar" v-for="(taskbar, index) in taskbars" :key="index" 
        :style="{ top: taskbar.top + 'px', left: taskbar.left + 'px', color: 'blue'}"
        @click="moveDetailsScrollRequest(taskbar)" :ref="taskbar.taskId"
        :class="{'box-active': taskbar.isActive}">
            <div class="task-bar-box" :style="{ width: taskbar.taskWidth + 'px' }">
                <div class="task-bar-name">{{ taskbar.taskName }} : {{ taskbar.progress }}%</div>
                <div class="task-bar-progress" :style="{ width: taskbar.taskProgressWidth + 'px'}"></div>
            </div>
        </div>
    </div>
    `,
    methods: {
        /** クリックされた要素のタスクと一致するtask_detailsに格納されている要素へスクロールしてもらう */
        moveDetailsScrollRequest(taskbar) {
            this.$emit('task-scroll', taskbar.taskId);
        },
        /** 指定されたタスクIDのタスクバーの存在する位置までスクロールする */
        scrollToSpecifiedTask(taskId){
            console.log('task-bar: ' + taskId);
            task = this.$refs[taskId][0];
            task.scrollIntoView({behavior: "smooth", block: "nearest", inline: "center"});
        }
    },
});

Vue.component('task-details', {
    props: ['tasks'],
    template: `
    <div>
        <div v-if="tasks.length === 0" class="task-ul-none">指定の期間のタスクは登録されていません</div>
        <ul class="task-ul">
            <li class="task-li" v-for="(task, index) in tasks" :key="index" :ref="task.taskId" 
            :class="{ active: task.isActive }" @click="scrollToSpecifiedTaskBar(task.taskId)">
                <div class="task-item">
                    <div class="task-left">
                        <div class="task-row">
                            <div class="task-label">タスク名:</div>
                            <div>{{ task.taskName }}</div>
                        </div>
                        <div class="task-row">
                            <div class="task-label">期間:</div>
                            <div>{{ task.startDate}} ~ {{ task.endDate }}</div>
                        </div>
                        <div class="task-row">
                            <div class="task-label">重要度:</div>
                            <div>{{ importanceValues[task.importance] }}</div>
                        </div>
                        <div class="task-row">
                            <div class="task-label">進捗度:</div>
                            <div>{{ task.progress }}%</div>
                        </div>
                        <div class="task-row">
                            <div class="task-label">詳細:</div>
                            <div>{{ task.detail }}</div>
                        </div>
                    </div>
                    
                    <div class="task-right">
                        <button class="btn-simple" @click="updateTaskRequest($event, task)">
                            <span class="material-icons">
                                text_snippet
                                </span>
                            <span>更新</span>
                        </button>
                        <button class="btn-simple" @click="deleteTaskRequest($event, task)">
                        <span class="material-icons">
                            delete
                            </span>
                        <span>削除</span>
                        </button>
                    </div>
                </div>
            </li>
        </ul>
    </div>
    `,
    methods: {
        /** 指定のタスクIDの格納されている部分までスクロールする */
        scrollToSpecifiedTask(taskId) {
            task = this.$refs[taskId][0];
            task.scrollIntoView({behavior: "smooth", block: "nearest", inline: "nearest"});
        },
        /** 指定のタスクIDの格納されているタスクバーの部分までスクロールする */
        scrollToSpecifiedTaskBar(taskId) {
            this.$emit('task-scroll', taskId);
        },
        /** 指定のタスクの更新処理のフォームを表示する */
        updateTaskRequest(event, task) {
            event.stopPropagation();
            formValidation.formAppendValue(task);
        },
        /** 指定のタスクの削除処理のフォームを表示する */
        deleteTaskRequest(event, task) {
            event.stopPropagation();
            deleteForm.openDeleteForm(task);
        }
    }
});

let now = new Date();

// taskの開始の最小値の日付データ
let minDate = getMinimumDateOfTasks(sample);
// taskの終了の最大値の日付データ
let maxDate = getMaxDateOfTasks(sample);

let dummyDate = new Date();
// 日付データのカレンダー
let myDates = minDate.length !== 0 ? getCalendarObjects(minDate, maxDate)
: getCalendarObjects(new Date(dummyDate.getFullYear(), dummyDate.getMonth(), 0),
 new Date(dummyDate.getFullYear(), dummyDate.getMonth() + 2, 0));

console.log(minDate, maxDate, myDates)

let taskBars = createTaskBars(sample);

console.log(taskBars)
console.log('myDates', myDates)
console.log('myDates[0][0]', myDates[0][0])

const app = new Vue({
    el: '#vue',
    data: {
        sample,
        sampleSize: sample.length,
        myDates,
        myDatesSize: myDates.length,
        taskBars,
        // thisEl: this.$el
    },
    methods: {
        getHeight() {
            console.log(this.$refs);
            console.log(this.$el)
        },
        /** タスクバーの開始位置の高さを求める */
        getTaskbarStartHeight() {
            // return this.$refs.calendar[0].$refs.year_month.offsetHeight +
            // this.$refs.calendar[0].$refs.day_inner[0].offsetHeight + 4;
            return 71;
        },
        /** タスクバーの配置位置のleftの位置を算出する */
        getTaskbarLeftPosition(startDate) {
            const firstDateObj = myDates[0][0];
            const minMyDate = new Date(firstDateObj.year, firstDateObj.month - 1, firstDateObj.day);
            const tmpDate = new Date(startDate);
            const taskBarStartDate = new Date(tmpDate.getFullYear(), tmpDate.getMonth(), tmpDate.getDate());
            
            const diffDate = getDiffDays(minMyDate, taskBarStartDate);
            
            const left = diffDate * 25; // 25は25px
            
            return left;
        },
        /** タスクバーの配置位置のtopの位置を算出する */
        getTaskbarTopPosition(index) {
            return (index - 1) * 20 + (index * 20) + this.getTaskbarStartHeight();
        },
        /** タスクバーの全体の長さを算出する */
        getTaskBarWidth(taskDays){
            return taskDays * 25;
        },
        /** タスクバーの進捗度の長さを算出する */
        getTaskBarProgressWidth(taskWidth, progress) {
            return Math.floor(taskWidth * (progress / 100));
        },
        /** タスクバーの配置位置の初期化 */
        initTaskBars(){
            this.taskBars.forEach(taskbar => {
                taskbar.left = this.getTaskbarLeftPosition(taskbar.startDate);
                taskbar.top = this.getTaskbarTopPosition(taskbar.index);
                taskbar.taskWidth = this.getTaskBarWidth(taskbar.taskDays);
                taskbar.taskProgressWidth = this.getTaskBarProgressWidth(taskbar.taskWidth, taskbar.progress);
            });
        },
        /** 
         * taskBar側から呼ばれる。指定のタスクIDをtaskDetails表が見える位置までスクロールし
         * 指定された要素どうしを5秒間ハイライトさせる
         */
        taskDetailsScrollRequest(taskId) {
            this.changeTaskBarsActive(taskId);
            this.$refs.task_details.scrollToSpecifiedTask(taskId);
        },
        /** 
         * taskDetails側から呼ばれる。指定のタスクIDをtaskBarの表が見える位置までスクロールする
         */
        taskBarScrollRequest(taskId) {
            this.changeTaskBarsActive(taskId);
            this.$refs.task_bar.scrollToSpecifiedTask(taskId);
        },
        /**
         * data taskBarsのisActiveを全てfalseで更新し、指定されたtaskIdを持つ
         * 要素のisActiveのみtrueに更新し、5秒後にtrueにした要素もfalseに変更する
         */
        changeTaskBarsActive(taskId) {
            this.taskBars.forEach(taskBar => taskBar.isActive = false);
            
            this.taskBars.forEach(taskBar => {
                if(taskBar.taskId === taskId){
                    taskBar.isActive = true;
                    setTimeout(() => {
                        taskBar.isActive = false;
                    }, 5000);
                }
            })
        }
    },
    mounted() {
        // console.log('mounted', this.$data)
        // console.log(this.getTaskbarStartHeight())
        
        // console.log(this.$refs.calendar[0].$refs.year_month.offsetHeight)
        // console.log(this.$refs.calendar[0].$refs.day_inner[0].offsetHeight)
    },
    created() {
        this.initTaskBars();
        // console.log('created', this.$data);
        // console.log(this.getTaskbarStartHeight())
    }
});