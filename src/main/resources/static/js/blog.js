let isRequesting = false

// 메뉴 활성화
document.addEventListener('DOMContentLoaded', function () {
    let pageNumber = 1;

    // 현재 페이지 URL
    let currentUrl = window.location.href;
    let decodedUrl = decodeURIComponent(currentUrl)

    // 모든 메뉴 항목 가져오기
    let menuItems = document.querySelectorAll('.side-link a')
    let menuName = decodedUrl.split("/")[1]

    // 각 메뉴 항목에 대해 현재 페이지와 비교하여 active 클래스 추가
    menuItems.forEach(function (menuItem) {
        let menuName = menuItem.textContent;
        if (decodedUrl.includes(menuName)) {
            menuItem.classList.add('active');
        }
    });

    $(window).scroll(function () {
        let scrollPosition = $(window).scrollTop();
        let documentHeight = $(document).height();
        let windowHeight = $(window).height();
        if (scrollPosition + windowHeight >= documentHeight && !isRequesting) {
            // 스크롤이 페이지의 끝에 도달했을 때 추가적인 동작을 수행할 수 있습니다.
            isRequesting = true;
            // setTimeout(() => isRequesting = false, 1000);
            getArticlesPagination(pageNumber++, menuName);
        }
    });

    // getArticlesPagination(pageNumber++, menuName);
});

const getArticlesPagination = async (pageNumber, menuName) => {
    const data = {};

    if (pageNumber) data["pageNumber"] = pageNumber;
    if (menuName) data["menuName"] = menuName;

    function success(result) {
        let articlesAppendHtml = "";
        let articles = result.data;
        articles.forEach(article => {
            articlesAppendHtml += `
                <div class="col">
                    <a href="/blog/${article.menuName}/${article.id}" class="link-offset-2 link-underline link-underline-opacity-0">
                        <div class="card border border-0 p-2 rounded-4">
                            <svg class="bd-placeholder-img card-img-top rounded-4" width="100%" height="180" xmlns="http://www.w3.org/2000/svg" role="img" aria-label="Placeholder:" preserveAspectRatio="xMidYMid slice" focusable="false"><title>Placeholder</title>
                                <rect width="100%" height="100%" fill="#55595c"></rect>
                                <text x="40%" y="50%" fill="#eceeef" dy=".3em">images</text>
                            </svg>
                            <div class="card-body">
                                <h5 class="mb-1">${article.title}</h5>
                                <p class="card-text text-body-tertiary fs-6">${article.subTitle}</p>
                            </div>
                        </div>
                    </a>
                </div>`;
        });
        $('#article-list').append(articlesAppendHtml);
        isRequesting = result.last;
    }

    function fail() {
    }

    await ajaxGetRequest('GET', '/api/articles', data, success, fail);
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
