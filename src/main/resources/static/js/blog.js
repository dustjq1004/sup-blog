const sendGetRequest = (menuId) => {
    const data = {
        "menuId": menuId
    };

    function success(fragment) {
        $('#article-list').replaceWith(fragment);
    }

    function fail() {
    }

    ajaxGetRequest('GET', '/articles', data, success, fail);
}

sendGetRequest();

function ajaxGetRequest(method, url, data, success, fail) {
    $.ajax({
        url: url,
        type: method,
        data: data,
        success: success,
        fail: fail
    });
}