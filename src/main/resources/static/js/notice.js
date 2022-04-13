const noticeVue = new Vue({
    el: '#head-notice',
    data: {
        noticeObjects: noticeObjects.noticeInformation,
        isEmpty: noticeObjects.noticeInformation.length === 0
    }
})