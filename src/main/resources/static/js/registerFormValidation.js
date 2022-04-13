class RegisterFormValidation extends FormValidation{
    constructor(formName, btnId, cancelBtnName  = '.btn-cancel') {
        super(formName, btnId, cancelBtnName);
    }

    init() {
        super.init();

        // 新規登録フォーム表示ボタン
        const taskAddBtn = document.querySelector('.main-head-add');
        // 新規登録表示ボタン押下時の処理
        taskAddBtn.addEventListener('click', event => {
        this.backContainer.classList.add(this.IN_VIEW);
        this.targetForm.classList.add(this.SELECT_FORM);
        });
    }

    // _backContainerAddEvent(target) {
    //     // バックの部分をクリックしたときに隠れるようcssのin-viewのクラスを削除する
    //     this.target.addEventListener('click', event => {
    //         this.backContainer.classList.remove(this.IN_VIEW);
    //         this.targetForm.classList.remove(this.SELECT_FORM);

    //         this.errorMessageObjects.forEach(obj => {
    //             obj.classList.remove(this.SHOW);
    //         });
    //     })
    // }
}

const registerForm = new RegisterFormValidation('register-form', '#register-btn');

registerForm.init();