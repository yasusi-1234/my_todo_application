function getOneMonthArray(dateObj) {
    const dateObjName = 'Date';
    const argObjName = Object.prototype.toString.call(dateObj).slice(8, -1);

    if(dateObjName !== argObjName){
        return [];
    }

    const dayOfFinal = new Date(dateObj.getFullYear(), dateObj.getMonth() + 1, 0).getDate();
    const year = dateObj.getFullYear();
    const month = dateObj.getMonth();

    let returnArray = [];
    for(let i = 1; i <= dayOfFinal; i++){
        returnArray.push(new Date(year, month, i));
    }
    return returnArray;
}

const dayOfWeeks = [ "日", "月", "火", "水", "木", "金", "土" ];

/**
 * 始まりの日付データと終わりの日付データを元に月ごとの日付データを算出し返却する
 * @param {*} startDate 
 * @param {*} endDate 
 * @returns 
 */
function getCalendarObjects(startDate, endDate) {
    if(startDate.getFullYear() === endDate.getFullYear()){
        // 年度をまたがない場合
        const year = startDate.getFullYear();
        const startMonth = startDate.getMonth();
        const endMonth = endDate.getMonth();
        const makeMonth = endMonth - startMonth + 1;

        let returnObjects = [];
        for(let i = startMonth; i <= endMonth; i++ ){
            let firstDate = new Date(year, i, 1);
            const dayOfFinal = new Date(year, i + 1, 0).getDate();
            
            let oneMonthObj = [];
            for(let j = 1; j <= dayOfFinal; j++){
                const oneDate = new Date(year, i, j);
                const dayOfWeek = dayOfWeeks[oneDate.getDay()];
                oneMonthObj.push({
                    year,
                    month: i + 1,
                    day: j,
                    dayOfWeek
                });
            }
            returnObjects.push(oneMonthObj);
        }
        return returnObjects;
    } else {
        // 年度をまたいだ場合 例 2021/12/01 ~ 2022/01/01等

        const startYear = startDate.getFullYear();
        const diffYear = endDate.getFullYear() - startYear;
        const makeMonth = (12 - startDate.getMonth()) + (endDate.getMonth() + 1) + ((diffYear - 1) * 12);

        let returnObjects = [];
        for(let i = 0; i < makeMonth; i++ ){
            const firstDate = new Date(startYear, startDate.getMonth() + i, 1);
            const dayOfFinal = new Date(firstDate.getFullYear(), firstDate.getMonth() + 1, 0).getDate();

            let oneMonthObj = [];
            for(let j = 1; j <= dayOfFinal; j++){
                const oneDate = new Date(firstDate.getFullYear(), firstDate.getMonth(), j);
                const dayOfWeek = dayOfWeeks[oneDate.getDay()];
                oneMonthObj.push({
                    year: oneDate.getFullYear(),
                    month: oneDate.getMonth() + 1,
                    day: j,
                    dayOfWeek
                });
            }
            returnObjects.push(oneMonthObj);
        }
        return returnObjects;
    }
}

/**
 * タスクリストのstartDateの一番新しい日付を取得し返却
 * @param {*} taskArray 
 * @returns 
 */
function getMinimumDateOfTasks(taskArray) {
    dateArr =  taskArray.map(task => new Date(task.startDate));
    
    return dateArr.length !== 0 ? dateArr.reduce((date1, date2) => date1 > date2 ? date2 : date1)
    : [];
}

/**
 * タスクリストのendDateの一番古い日付を取得し返却
 * @param {*} taskArray 
 * @returns 
 */
function getMaxDateOfTasks(taskArray) {
    dateArr =  taskArray.map(task => new Date(task.endDate));

    return dateArr.length !== 0 ? dateArr.reduce((date1, date2) => date1 < date2 ? date2 : date1)
    : [];
}

/**
 * startDateからendDateの差分の日数を求める
 * @param {*} startDate 
 * @param {*} endDate 
 * @returns 
 */
function getDiffDays(startDate, endDate) {
    // (1000ミリ秒 * 60 * 60 * 24) = 1日 ※1000ミリ秒,60秒,60分,24時間
    const sd = new Date(startDate);
    const ed = new Date(endDate);
    const dateDiff = sd < ed ? ed - sd : sd - ed; 
    return dateDiff / (1000 * 60 * 60 * 24);
}
