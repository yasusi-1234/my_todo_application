class DeleteForm {
    constructor(formName = 'delete-form', btnId ='#delete-btn', cancelBtnName = '.btn-cancel') {
        this.targetForm = document.forms[formName];

        this.taskId = this.targetForm.taskId;

        this.taskNameHTML = this.targetForm.querySelector('#delete-task-name');
        this.startDateHTML = this.targetForm.querySelector('#delete-start-date');
        this.endDateHTML = this.targetForm.querySelector('#delete-end-date');
        this.detailHTML = this.targetForm.querySelector('#delete-task-detail');
        this.importanceHTML = this.targetForm.querySelector('#delete-importance');
        this.progressHTML = this.targetForm.querySelector('#delete-progress');

        this.btn = this.targetForm.querySelector(btnId);

        this.cancelBtn = this.targetForm.querySelector(cancelBtnName);

        // フォームの裏に来るコンテナオブジェクト
        this.backContainer = document.querySelector('#back-container');

        /** in-view */
        this.IN_VIEW = 'in-view';
        /** select-form */
        this.SELECT_FORM = 'select-form';
    }

    init() {
        // フォーム部分のクリック時にイベントを伝播させない為の処置
        this.targetForm.addEventListener('click', event => event.stopPropagation());

        this._backContainerAddEvent(this.backContainer);
        this._backContainerAddEvent(this.cancelBtn);

        // // 新規登録フォーム表示ボタン
        // const searchAddBtn = document.querySelector('.main-head-search');
        // // 新規登録表示ボタン押下時の処理
        // searchAddBtn.addEventListener('click', event => {
        // this.backContainer.classList.add(this.IN_VIEW);
        // this.targetForm.classList.add(this.SELECT_FORM);
        // });
    }

    openDeleteForm(task){
        this.taskId.value = task.taskId;

        this.taskNameHTML.innerHTML = task.taskName;
        this.startDateHTML.innerHTML = task.startDate; 
        this.endDateHTML.innerHTML = task.endDate;
        this.detailHTML.innerHTML = task.detail;
        this.importanceHTML.innerHTML = task.importance;
        this.progressHTML.innerHTML = task.progress;

        this.backContainer.classList.add(this.IN_VIEW);
        this.targetForm.classList.add(this.SELECT_FORM);
    }

    _backContainerAddEvent(target) {
        // バックの部分をクリックしたときに隠れるようcssのin-viewのクラスを削除する
        target.addEventListener('click', event => {
            this.backContainer.classList.remove(this.IN_VIEW);
            this.targetForm.classList.remove(this.SELECT_FORM);
        })
        
    }

}

const deleteForm = new DeleteForm();
deleteForm.init();