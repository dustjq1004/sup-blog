// 널 체크 함수
// 널 || 길이가 0 || 'null'
function isNull(checkValue) {
    if (checkValue == null || checkValue === 'undefined') {
        return true;
    }

    if (typeof checkValue === "string") {
        return checkValue.trim().length === 0 || checkValue === 'null'
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
            httpRequest(url, options, success, fail);
        }
    } else {
        return fail(response);
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

// FormData Json 변환
$.fn.serializeFormToJSON = function () {
    const json = {};

    // Select 요소의 선택된 값만 처리
    this.find('select').each(function () {
        const name = $(this).attr('name');
        const selectedValue = $(this).find(':selected').val() || '';
        if (name) {
            json[name] = selectedValue;
        }
    });

    // 기타 입력 요소 처리
    const formData = this.serializeArray();
    $.each(formData, function () {
        if (!json.hasOwnProperty(this.name)) {
            // Select에서 처리되지 않은 다른 요소만 추가
            json[this.name] = this.value || '';
        }
    });

    return json;
}

function nvl(value) {
    return value ?? "";
}

$(document).on('show.bs.modal shown.bs.modal', function () {
    $('body').css('padding-right', '');

    // body scrollbar 제거
    $('html').css('overflow', 'hidden');
});

$(document).on('hidden.bs.modal', function () {
    $('html').css('overflow', '');
});

// DOM 로딩 완료 후 애니메이션 적용
document.addEventListener('DOMContentLoaded', function () {
    // body에 클래스 추가 (CSS에서 body에 직접 적용했다면 불필요)
    //document.body.classList.add('page-fade-in');

    // 또는 특정 요소들에 순차적으로 적용
    const fadeElements = document.querySelectorAll('.fade-in-element');
    fadeElements.forEach((element, index) => {
        setTimeout(() => {
            element.style.opacity = '1';
            element.style.transform = 'translateY(0)';
        }, index * 100); // 각 요소마다 100ms 지연
    });
});

