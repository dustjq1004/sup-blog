// 널 체크 함수
// 널 || 길이가 0 || 'null'
function isNull(checkValue) {
    if (checkValue == null || checkValue === 'undefined') {
        return true;
    }

    if (checkValue instanceof String) {
        return checkValue.trim().length == 0 || checkValue === 'null'
    }

    return false;
}


// HTTP 요청을 보내는 함수
async function httpRequest(url, options, success, fail) {
    if (isNull(options.headers)) {
        options.headers = {};
    }

    options.headers['Authorization'] = 'Bearer ' + localStorage.getItem('access_token');
    if (isNull(options.headers['Content-Type'])) {
        options.headers['Content-Type'] = 'application/json';
    }

    const response = await fetch(url, options);

    if (response.status === 200 || response.status === 201) {
        return success(response);
    }

    const refresh_token = getCookie('refresh_token');
    if (response.status === 401 && refresh_token) {
        const result = await requestAccessToken(getCookie('refresh_token'));
        if (result) {
            httpRequest(method, url, body, success, fail);
        }
    } else {
        return fail();
    }
}

// refresh token 요청
async function requestAccessToken(refresh_token) {
    const response = await fetch('/api/token', {
        method: 'POST',
        headers: {
            Authorization: 'Bearer ' + localStorage.getItem('access_token'),
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            refreshToken: refresh_token,
        }),
    });

    if (response.ok) {
        const responseData = response.json();
        localStorage.setItem('access_token', responseData.accessToken);
        return response.ok;
    }
}

// 쿠키를 가져오는 함수
function getCookie(key) {
    var result = null;
    var cookie = document.cookie.split(';');
    cookie.some(function (item) {
        item = item.replace(' ', '');

        var dic = item.split('=');

        if (key === dic[0]) {
            result = dic[1];
            return true;
        }
    });

    return result;
}