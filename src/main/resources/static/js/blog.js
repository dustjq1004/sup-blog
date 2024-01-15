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

const getArticle = (articleId) => {
    function success(fragment) {
        $('#main').html(fragment);
    }

    function fail() {
    }

    ajaxGetRequest('GET', '/blog/articles/' + articleId, null, success, fail);
}


getArticles();

function ajaxGetRequest(method, url, data, success, fail) {
    $.ajax({
        url: url,
        type: method,
        data: data,
        success: success,
        fail: fail
    });
}