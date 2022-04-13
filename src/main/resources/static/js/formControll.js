const backContainer = document.querySelector('#back-container');
// 更新用フォームオブジェクト
const updateForm = document.forms['updateForm'];
// 新規登録用フォームオブジェクト
const registerForm = document.querySelector('#register-form');
// 新規登録フォーム表示ボタン
const taskAddBtn = document.querySelector('.main-head-add');
// フォーム部分のクリック時にイベントを伝播させない為の処置
updateForm.addEventListener('click', event => event.stopPropagation());
registerForm.addEventListener('click', event => event.stopPropagation());

// 更新用フォームの各インプット要素
const updateTaskId = updateForm.taskId;
const updateTaskName = updateForm.taskName;
const updateStartDate = updateForm.startDate;
const updateEndDate = updateForm.endDate;
const updateDetail = updateForm.detail;
const updateImportance = updateForm.importance;
const updateProgress = updateForm.progress;
const updateNotice = updateForm.notice;
/** 更新用ボタン */
const updateBtn = updateForm.querySelector('#updateBtn');

// 更新用フォームのエラー情報要素
// 0: taskName, 1: startDate, 2: endDate, 3: importance, 4: progress, 5: notice
const errorMessageObjects = updateForm.querySelectorAll('.form-error');
console.log(errorMessageObjects)


// バックの部分をクリックしたときに隠れるようcssのin-viewのクラスを削除する
backContainer.addEventListener('click', event =>{
    backContainer.classList.remove('in-view');
    updateForm.classList.remove('select-form');
    registerForm.classList.remove('select-form');

    errorMessageObjects.forEach(obj => {
        obj.classList.remove('show');
    });

    updateTaskName.classList.remove('error');
    updateStartDate.classList.remove('error');
    updateEndDate.classList.remove('error');
    updateImportance.classList.remove('error');
    updateProgress.classList.remove('error');
    // updateNotice.classList.remove('error');
})

/** 渡されたtask情報をinputのvalueに設定する */
function formHelp(taskObj) {
    // バックに隠れているフォームを表示するための処理
    backContainer.classList.add('in-view')
    updateForm.classList.add('select-form');

    updateTaskId.value = taskObj.taskId;
    updateTaskName.value = taskObj.taskName;
    updateStartDate.value = taskObj.startDate;
    updateEndDate.value = taskObj.endDate;
    updateDetail.value = taskObj.detail;
    updateImportance.value = taskObj.importance;
    updateProgress.value = taskObj.progress;
    updateNotice.value = taskObj.notice;
}

// 新規登録表示ボタン押下時の処理
taskAddBtn.addEventListener('click', event => {
    backContainer.classList.add('in-view');
    registerForm.classList.add('select-form');
});

// validation
updateTaskName.addEventListener('blur', event => {
    selectValidation(updateTaskName, errorMessageObjects, 0, textValidate);
})

updateProgress.addEventListener('blur', event => {
    selectValidation(updateProgress, errorMessageObjects, 4, numberValidate);
})

updateImportance.addEventListener('blur', event => {
    selectValidation(updateImportance, errorMessageObjects, 3, importanceValidate);
})

Array(updateStartDate, updateEndDate).forEach(el => {
    el.addEventListener('blur', event => {
        const { hasError, message } = dateValidate(updateStartDate.value, updateEndDate.value);
        console.log(hasError, message)
        if(hasError){
            updateStartDate.classList.add('error');
            errorMessageObjects[1].classList.add('show');
            errorMessageObjects[1].innerHTML = 
                message === '' ? '※ 終了時期より前の日を選択してください' : message;
            
            updateEndDate.classList.add('error');
            errorMessageObjects[2].classList.add('show');
            errorMessageObjects[2].innerHTML = 
                message === '' ? '※ 開始時期より後の日を選択してください' : message;
        } else {
            updateStartDate.classList.remove('error');
            errorMessageObjects[1].classList.remove('show');
    
            updateEndDate.classList.remove('error');
            errorMessageObjects[2].classList.remove('show');
        }
    })
})

updateForm.addEventListener('submit', event => {
    const tnHasError = selectValidation(updateTaskName, errorMessageObjects, 0, textValidate);
    const pHasError = selectValidation(updateProgress, errorMessageObjects, 4, numberValidate);
    const iHashError = selectValidation(updateImportance, errorMessageObjects, 3, importanceValidate);
    const {hasError, message, sdInvalid, edInvalid} = dateValidateOfBtn(updateStartDate.value, updateEndDate.value);

    if(hasError){
        // 日付関連にエラー有り
        if(sdInvalid){
            updateStartDate.classList.add('error');
            errorMessageObjects[1].classList.add('show');
            errorMessageObjects[1].innerHTML = message;
        }

        if(edInvalid){
            updateEndDate.classList.add('error');
            errorMessageObjects[2].classList.add('show');
            errorMessageObjects[2].innerHTML = message;
        }

        if(!sdInvalid && !edInvalid){
            updateStartDate.classList.add('error');
            errorMessageObjects[1].classList.add('show');
            errorMessageObjects[1].innerHTML = message;
            updateEndDate.classList.add('error');
            errorMessageObjects[2].classList.add('show');
            errorMessageObjects[2].innerHTML = message;
        }
    }

    if(tnHasError || pHasError || iHashError || hasError){
        // 何れかにエラーが見つかった場合
        event.preventDefault();
    }
})

/** 検証した上で指定された要素にクラス等を反映させる */
function selectValidation(target, errorElements, index, cbValid) {
    const result = cbValid(target.value);
    if(result){
        errorElements[index].classList.add('show');
        target.classList.add('error');
    }else {
        errorElements[index].classList.remove('show');
        target.classList.remove('error');
    }
    return result;
}

// updateTaskId 
// updateTaskName
// updateStartDate
// updateEndDate
// updateDetail
// updateImportance
// updateProgress
// updateNotice
// updateBtn 

/** 引数の数値チェック */
function numberValidate(numberValue) {
    console.log(numberValue < 0 || numberValue > 100);
    return Number.isNaN(numberValue) ||numberValue < 0 || numberValue > 100;
}

/** 引数の文字列チェック */
function textValidate(textValue) {
    // 空白文字で終始するパターン
    r = /^[ 　]+$/;
    return textValue === '' || r.test(textValue) || textValue === undefined || textValue === null;
}

/** 日付の引数チェック */
function dateValidate(startDate, endDate) {
    let sd = new Date(startDate);
    let ed = new Date(endDate);

    if(isInvalidDate(sd) && isInvalidDate(ed)){
        // 両方未入力の場合
        console.log('error');
        return { hasError: true, message: '※ 開始時期及び終了時期は入力必須です'};
    } else if(isInvalidDate(sd) || isInvalidDate(ed)){
        // どちらか片方が未入力
        console.log('one side isInvalid');
        return { hasError: false, message: ''};
    } else {
        // どちらも入力済み
        console.log('true is error: ', sd >= ed);
        return { hasError: sd >= ed, message: ''};
    }
}

/** Dateオブジェクトが正しいかどうかのチェック */
function isInvalidDate(DateObj) {
    return Number.isNaN(DateObj.getTime());
}

/** importance の値チェック */
function importanceValidate(val) {
    console.log(val, val === '' || val === undefined || val === null);
    return val === '' || val === undefined || val === null;
}

/** noticeの値チェック */
function noticeValidate(val) {
    return val !== 'true' || val !== 'false';
}

/** 日付の引数チェック(ボタン用) */
function dateValidateOfBtn(startDate, endDate) {
    const sd = new Date(startDate);
    const ed = new Date(endDate);

    const sdInvalid = isInvalidDate(sd);
    const edInvalid = isInvalidDate(ed);
    if(sdInvalid || edInvalid){
        // 未入力項目がある場合
        return { hasError: true, message: '※ 開始時期及び終了時期は入力必須です', sdInvalid, edInvalid};
    } else {
        // どちらも入力済み
        return { hasError: sd >= ed, message: '※ 開始時期及び終了時期は入力必須です', sdInvalid, edInvalid};
    }
}