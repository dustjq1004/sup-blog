// 메뉴 활성화
document.addEventListener('DOMContentLoaded', function () {
    // 현재 페이지 URL
    let currentUrl = window.location.href;
    let decodedUrl = decodeURIComponent(currentUrl);

    // 모든 메뉴 항목 가져오기
    let menuItems = document.querySelectorAll('.side-link a');

    // 각 메뉴 항목에 대해 현재 페이지와 비교하여 active 클래스 추가
    menuItems.forEach(function (menuItem) {
        let menuName = menuItem.textContent;
        if (decodedUrl.includes(menuName)) {
            menuItem.classList.add('active');
        }
    });
});

const getArticles = (menuId) => {
    const data = {
        "menuId": menuId
    };

    function success(fragment) {
        $('#main').html(fragment);
    }

    function fail() {
    }

    ajaxGetRequest('GET', '/blog/articles', data, success, fail);
}

function ajaxGetRequest(method, url, data, success, fail) {
    $.ajax({
        url: url,
        type: method,
        data: data,
        success: success,
        fail: fail
    });
}