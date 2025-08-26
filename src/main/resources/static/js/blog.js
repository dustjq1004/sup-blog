let isRequesting = false

// 메뉴 활성화
document.addEventListener('DOMContentLoaded', function () {
    let pageNumber = 0;

    // 현재 페이지 URL
    let menuItems = document.querySelectorAll('.side-link a');
    let currentUrl = window.location.href;
    let decodedUrl = decodeURIComponent(currentUrl)

    // 모든 메뉴 항목 가져오기
    let menuName = decodedUrl.split("/")[4];

    // 각 메뉴 항목에 대해 현재 페이지와 비교하여 active 클래스 추가
    menuItems.forEach(function (menuItem) {
        let menuName = menuItem.getAttribute('data-menu-name');
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

    // 검색 버튼 클릭
    $('#blog-search-btn').on('click', function () {
        $('#article-list').html("");
        getArticlesPagination(0, menuName);
    });

    // input에서 Enter 키 입력 시
    $('#blog-search-input').on('keydown', function (e) {
        if (e.key === 'Enter') {
            $('#article-list').html("");
            getArticlesPagination(0, menuName);
        }
    });

    getArticlesPagination(pageNumber++, menuName);
});

const getArticlesPagination = async (pageNumber, menuName) => {
    const data = {};

    if (pageNumber) data["pageNumber"] = pageNumber;
    if (menuName) data["menuName"] = menuName;
    data["searchKeyword"] = $('#blog-search-input').val();

    showSkeletonLoading('#article-list');

    function success(result) {
        let articlesAppendHtml = ""
        let articles = result.data
        if (articles.length === 0) {
            articlesAppendHtml += `
                <div class="empty-article-message">
                  <div class="empty-article-icon">
                    <!-- Bootstrap 아이콘 사용: 느낌표 원형 -->
                    <svg width="64" height="64" viewBox="0 0 64 64" fill="none">
                      <circle cx="32" cy="32" r="30" stroke="#3B82F6" stroke-width="3"/>
                      <text x="32" y="44" text-anchor="middle" font-size="40" fill="#3B82F6" font-family="Arial, sans-serif">!</text>
                    </svg>
                  </div>
                  <div class="empty-article-text">
                    조회된 블로그 글이 없습니다.
                  </div>
                </div>
            `
        }
        articles.forEach(article => {
            articlesAppendHtml += `
                <div class="col">
                    <a href="/blog/${article.menuName}/${article.id}" class="link-offset-2 link-underline link-underline-opacity-0">
                        <div class="card border border-0 p-2 rounded-4">
                            <svg class="bd-placeholder-img card-img-top rounded-4" width="100%" height="180"
                                 xmlns="http://www.w3.org/2000/svg" role="img"
                                 preserveAspectRatio="xMidYMid slice" focusable="false"><title></title>
                                <image height="100%" width="100%"
                                       preserveAspectRatio="xMidYMid slice"
                                       href="${article.thumbnailUrl}"></image>

                            </svg>
                            <div class="card-body">
                                <div class="mb-2">
                                    <span class="text-secondary fs-7"
                                          text="${moment(article.updatedAt).format("YYYY.MM.DD")}"></span>
                                </div>
                                <h5 class="mb-1 text-truncate">${article.title}</h5>
                                <p class="card-text text-body-tertiary fs-6 text-truncate">${article.subTitle}</p>
                            </div>
                        </div>
                    </a>
                </div>`;
        });
        $('#article-list').append(articlesAppendHtml);
        isRequesting = result.last;
        $('.skeleton-card').remove();
    }

    function fail() {
    }

    setTimeout(async () => {
        await ajaxGetRequest('GET', '/api/articles', data, success, fail);
    }, 1000);


    // hideSkeletonLoading();
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

