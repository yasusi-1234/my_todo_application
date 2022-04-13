class SearchFormValidation {
    constructor(formName, btnId, cancelBtnName = '.btn-cancel') {
        this.targetForm = document.forms[formName];
        
        this.taskName = this.targetForm.taskName;
        this.startDate = this.targetForm.startDate;
        this.endDate = this.targetForm.endDate;
        this.detail = this.targetForm.detail;
        this.importance = this.targetForm.importance;
        this.progress = this.targetForm.progress;

        this.btn = this.targetForm.querySelector(btnId);

        this.cancelBtn = this.targetForm.querySelector(cancelBtnName);

        /** .form-error */
        this.FORM_ERROR = '.form-error';
        /** in-view */
        this.IN_VIEW = 'in-view';
        /** select-form */
        this.SELECT_FORM = 'select-form';
        /** show */
        this.SHOW = 'show';
        /** error */
        this.ERROR = 'error';
        // フォームのエラー情報要素
        // 1: startDate, 2: endDate, 4: importance
        this.errorMessageObjects = this.targetForm.querySelectorAll(this.FORM_ERROR);

        // フォームの裏に来るコンテナオブジェクト
        this.backContainer = document.querySelector('#back-container');
    }

    init() {
        // フォーム部分のクリック時にイベントを伝播させない為の処置
        this.targetForm.addEventListener('click', event => event.stopPropagation());

        this._backContainerAddEvent(this.backContainer);
        this._backContainerAddEvent(this.cancelBtn);

        /** フォームのボタンが押下されたときの処理 */
        this.targetForm.addEventListener('submit', event => {
            const hasError = this._dateValidate(this.startDate.value, this.endDate.value);
            
            if (hasError) {
                // 日付関連にエラー有り
                this.startDate.classList.add(this.ERROR);
                this.endDate.classList.add(this.ERROR);
                this.errorMessageObjects[1].classList.add(this.SHOW);
                this.errorMessageObjects[2].classList.add(this.SHOW);
                
                event.preventDefault();
            }
        })

        // 新規登録フォーム表示ボタン
        const searchBtn = document.querySelector('.main-head-search');
        // 新規登録表示ボタン押下時の処理
        searchBtn.addEventListener('click', event => {
        this.backContainer.classList.add(this.IN_VIEW);
        this.targetForm.classList.add(this.SELECT_FORM);
        });
    }

    _backContainerAddEvent(target) {
        // バックの部分をクリックしたときに隠れるようcssのin-viewのクラスを削除する
        target.addEventListener('click', event => {
            this.backContainer.classList.remove(this.IN_VIEW);
            this.targetForm.classList.remove(this.SELECT_FORM);

            this.errorMessageObjects.forEach(obj => {
                obj.classList.remove(this.SHOW);
            });

            this.startDate.classList.remove(this.ERROR);
            this.endDate.classList.remove(this.ERROR);
        })
        
    }

    /** 日付の引数チェック */
    _dateValidate(startDate, endDate) {
        let sd = new Date(startDate);
        let ed = new Date(endDate);

        if (!this._isInvalidDate(sd) && !this._isInvalidDate(ed)) {
            // 両方入力されている場合
            return sd > ed;
        } 
        return false;
    }

    /** Dateオブジェクトが正しいかどうかのチェック */
    _isInvalidDate(DateObj) {
        return Number.isNaN(DateObj.getTime());
    }

}

const searchForm = new SearchFormValidation('search-form', '#search-btn');
searchForm.init();