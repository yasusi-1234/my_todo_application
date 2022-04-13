class FormValidation {
    constructor(formName, btnId, cancelBtnName = '.btn-cancel') {
        this.targetForm = document.forms[formName];
        this.taskId = this.targetForm.taskId;
        this.taskName = this.targetForm.taskName;
        this.startDate = this.targetForm.startDate;
        this.endDate = this.targetForm.endDate;
        this.detail = this.targetForm.detail;
        this.importance = this.targetForm.importance;
        this.progress = this.targetForm.progress;
        this.notice = this.targetForm.notice;
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
        // 0: taskName, 1: startDate, 2: endDate, 3: importance, 4: progress, 5: notice
        this.errorMessageObjects = this.targetForm.querySelectorAll(this.FORM_ERROR);

        // フォームの裏に来るコンテナオブジェクト
        this.backContainer = document.querySelector('#back-container');
    }

    init() {
        // フォーム部分のクリック時にイベントを伝播させない為の処置
        this.targetForm.addEventListener('click', event => event.stopPropagation());

        this._backContainerAddEvent(this.backContainer);
        this._backContainerAddEvent(this.cancelBtn);

        // validation
        this.taskName.addEventListener('blur', event => {
            this._selectValidation(this.taskName, this.errorMessageObjects, 0, this._textValidate);
        })

        this.progress.addEventListener('blur', event => {
            this._selectValidation(this.progress, this.errorMessageObjects, 4, this._numberValidate);
        })

        this.importance.addEventListener('blur', event => {
            this._selectValidation(this.importance, this.errorMessageObjects, 3, this._importanceValidate);
        })

        Array(this.startDate, this.endDate).forEach(el => {
            el.addEventListener('blur', event => {
                const {
                    hasError,
                    message
                } = this._dateValidate(this.startDate.value, this.endDate.value);
                console.log(hasError, message)
                if (hasError) {
                    this.startDate.classList.add(this.ERROR);
                    this.errorMessageObjects[1].classList.add(this.SHOW);
                    this.errorMessageObjects[1].innerHTML =
                        message === '' ? '※ 終了時期より前の日を選択してください' : message;

                    this.endDate.classList.add(this.ERROR);
                    this.errorMessageObjects[2].classList.add(this.SHOW);
                    this.errorMessageObjects[2].innerHTML =
                        message === '' ? '※ 開始時期より後の日を選択してください' : message;
                } else {
                    this.startDate.classList.remove(this.ERROR);
                    this.errorMessageObjects[1].classList.remove(this.SHOW);

                    this.endDate.classList.remove(this.ERROR);
                    this.errorMessageObjects[2].classList.remove(this.SHOW);
                }
            })
        })

        /** フォームのボタンが押下されたときの処理 */
        this.targetForm.addEventListener('submit', event => {
            const tnHasError = this._selectValidation(this.taskName, this.errorMessageObjects, 0, this._textValidate);
            const pHasError = this._selectValidation(this.progress, this.errorMessageObjects, 4, this._numberValidate);
            const iHashError = this._selectValidation(this.importance, this.errorMessageObjects, 3, this._importanceValidate);
            const {
                hasError,
                message,
                sdInvalid,
                edInvalid
            } = this._dateValidateOfBtn(this.startDate.value, this.endDate.value);

            if (hasError) {
                // 日付関連にエラー有り
                if (sdInvalid) {
                    this.startDate.classList.add(this.ERROR);
                    this.errorMessageObjects[1].classList.add(this.SHOW);
                    this.errorMessageObjects[1].innerHTML = message;
                }

                if (edInvalid) {
                    this.endDate.classList.add(this.ERROR);
                    this.errorMessageObjects[2].classList.add(this.SHOW);
                    this.errorMessageObjects[2].innerHTML = message;
                }

                if (!sdInvalid && !edInvalid) {
                    this.startDate.classList.add(this.ERROR);
                    this.errorMessageObjects[1].classList.add(this.SHOW);
                    this.errorMessageObjects[1].innerHTML = message;
                    this.endDate.classList.add(this.ERROR);
                    this.errorMessageObjects[2].classList.add(this.SHOW);
                    this.errorMessageObjects[2].innerHTML = message;
                }
            }

            if (tnHasError || pHasError || iHashError || hasError) {
                // 何れかにエラーが見つかった場合
                event.preventDefault();
            }
        })
    }

    _backContainerAddEvent(target) {
        // バックの部分をクリックしたときに隠れるようcssのin-viewのクラスを削除する
        target.addEventListener('click', event => {
            this.backContainer.classList.remove(this.IN_VIEW);
            this.targetForm.classList.remove(this.SELECT_FORM);

            this.errorMessageObjects.forEach(obj => {
                obj.classList.remove(this.SHOW);
            });

            this.taskName.classList.remove(this.ERROR);
            this.startDate.classList.remove(this.ERROR);
            this.endDate.classList.remove(this.ERROR);
            this.importance.classList.remove(this.ERROR);
            this.progress.classList.remove(this.ERROR);
        })
        
    }

    /** 渡されたtask情報を各input要素のvalueに設定し表示する */
    formAppendValue(taskObj) {
        // バックに隠れているフォームを表示するための処理
        this.backContainer.classList.add('in-view')
        this.targetForm.classList.add('select-form');

        this.taskId.value = taskObj.taskId;
        this.taskName.value = taskObj.taskName;
        this.startDate.value = taskObj.startDate;
        this.endDate.value = taskObj.endDate;
        this.detail.value = taskObj.detail;
        this.importance.value = taskObj.importance;
        this.progress.value = taskObj.progress;
        this.notice.value = taskObj.notice;
    }

    /** 検証した上で指定された要素にクラス等を反映させる */
    _selectValidation(target, errorElements, index, cbValid) {
        const result = cbValid(target.value);
        if (result) {
            errorElements[index].classList.add(this.SHOW);
            target.classList.add(this.ERROR);
        } else {
            errorElements[index].classList.remove(this.SHOW);
            target.classList.remove(this.ERROR);
        }
        return result;
    }

    /** 引数の数値チェック */
    _numberValidate(numberValue) {
        console.log(numberValue < 0 || numberValue > 100);
        return Number.isNaN(numberValue) || numberValue < 0 || numberValue > 100;
    }

    /** 引数の文字列チェック */
    _textValidate(textValue) {
        // 空白文字で終始するパターン
        const r = /^[ 　]+$/;
        return textValue === '' || r.test(textValue) || textValue === undefined || textValue === null;
    }

    /** 日付の引数チェック */
    _dateValidate(startDate, endDate) {
        let sd = new Date(startDate);
        let ed = new Date(endDate);

        if (this._isInvalidDate(sd) && this._isInvalidDate(ed)) {
            // 両方未入力の場合
            return {
                hasError: true,
                message: '※ 開始時期及び終了時期は入力必須です'
            };
        } else if (this._isInvalidDate(sd) || this._isInvalidDate(ed)) {
            // どちらか片方が未入力
            return {
                hasError: false,
                message: ''
            };
        } else {
            // どちらも入力済み
            return {
                hasError: sd >= ed,
                message: ''
            };
        }
    }

    /** 日付の引数チェック(ボタン用) */
    _dateValidateOfBtn(startDate, endDate) {
        const sd = new Date(startDate);
        const ed = new Date(endDate);

        const sdInvalid = this._isInvalidDate(sd);
        const edInvalid = this._isInvalidDate(ed);
        if (sdInvalid || edInvalid) {
            // 未入力項目がある場合
            return {
                hasError: true,
                message: '※ 開始時期及び終了時期は入力必須です',
                sdInvalid,
                edInvalid
            };
        } else {
            // どちらも入力済み
            return {
                hasError: sd >= ed,
                message: '※ 開始時期及び終了時期は入力必須です',
                sdInvalid,
                edInvalid
            };
        }
    }

    /** Dateオブジェクトが正しいかどうかのチェック */
    _isInvalidDate(DateObj) {
        return Number.isNaN(DateObj.getTime());
    }

    /** importance の値チェック */
    _importanceValidate(val) {
        return val === '' || val === undefined || val === null;
    }

    /** noticeの値チェック */
    _noticeValidate(val) {
        return val !== 'true' || val !== 'false';
    }


}


const formValidation = new FormValidation('updateForm', '#updateBtn');
formValidation.init();