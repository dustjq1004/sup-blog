const easyMDE = new EasyMDE({
    status: ["lines", "words"],
    spellChecker: false,
    element: $("#content")[0],
    promptURLs: true,
    sideBySideFullscreen: false,
    toolbarButtonClassPrefix: "mde",
    imageUploadEndpoint: '/',
    uploadImage: true,
    toolbar: [
        "bold", "italic", "strikethrough", "heading", "|",
        "code", "quote", "unordered-list", "ordered-list", "table", "|",
        "upload-image", "link", "|",
        "preview", "side-by-side"
    ],
    theme: "Modern",
    imageUploadFunction:
        async (file, onSuccess, onError) => {
            const result = await getResignedUrl(file.name);
            const presigendUrl = result.signedUrl;
            const s3Data = await requestS3PresignedImageUpload(presigendUrl, file.type, file);
            onSuccess(s3Data.url.split("?")[0]);
        }
});

const getResignedUrl = async (fileName) => {
    const options = {
        method: "GET",
        headers: {
            "Content-type": "application/json"
        }
    }
    const response = await fetch("/api/s3/image?fileName=" + fileName, options);
    return response.json();
}

const requestS3PresignedImageUpload = async (presigendUrl, type, file) => {
    const response = await fetch(presigendUrl, {
        method: "PUT",
        body: file,
        headers: {
            "Content-type": type
        }
    });
    return response;
}

// easyMDE.codemirror.on("paste", function () {
//     var items = (event.clipboardData || event.originalEvent.clipboardData).items;
//     console.log(JSON.stringify(items));
//     for (index in items) {
//         var item = items[index];
//         if (item.kind === 'file') {
//             var blob = item.getAsFile();
//             var reader = new FileReader();
//             reader.onload = function (event) {
//                 // data url!
//                 // console.log(event.target.result)
//             };
//             reader.readAsDataURL(blob);
//         }
//     }
// });

// 삭제 기능
const deleteButton = document.getElementById('delete-btn');

if (deleteButton) {
    deleteButton.addEventListener('click', event => {
        let id = document.getElementById('article-id').value;

        function success() {
            alert('삭제가 완료되었습니다.');
            location.replace('/blog');
        }

        function fail() {
            alert('삭제 실패했습니다.');
            location.replace('/blog');
        }

        httpRequest('DELETE', `/api/articles/${id}`, null, success, fail);
    });
}

// 수정 기능
const modifyButton = document.getElementById('modify-btn');

if (modifyButton) {
    modifyButton.addEventListener('click', event => {
        let params = new URLSearchParams(location.search);
        let id = params.get('id');
        const content = easyMDE.value()
        ;
        console.log(content);
        body = JSON.stringify({
            title: document.getElementById('title').value,
            content: content,
            menuId: document.getElementById('menu').value
        });

        function success() {
            alert('수정 완료되었습니다.');
            location.replace(`/articles/${id}`);
        }

        function fail() {
            alert('수정 실패했습니다.');
            location.replace(`/articles/${id}`);
        }

        httpRequest('PUT', `/api/articles/${id}`, body, success, fail);
    });
}

// 생성 기능
const createButton = document.getElementById('create-btn');

if (createButton) {
    // 등록 버튼을 클릭하면 /api/articles로 요청을 보낸다
    createButton.addEventListener('click', event => {
        const content = easyMDE.value();
        console.log(content);
        body = JSON.stringify({
            title: document.getElementById('title').value,
            content: content,
            menuId: document.getElementById('menu').value
        });

        function success() {
            alert('등록 완료되었습니다.');
            location.replace('/blog');
        };

        function fail() {
            alert('등록 실패했습니다.');
            location.replace('/blog');
        };

        httpRequest('POST', '/api/articles', body, success, fail)
    });
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

// HTTP 요청을 보내는 함수
function httpRequest(method, url, body, success, fail) {
    fetch(url, {
        method: method,
        headers: { // 로컬 스토리지에서 액세스 토큰 값을 가져와 헤더에 추가
            Authorization: 'Bearer ' + localStorage.getItem('access_token'),
            'Content-Type': 'application/json',
        },
        body: body,
    }).then(response => {
        if (response.status === 200 || response.status === 201) {
            return success();
        }
        const refresh_token = getCookie('refresh_token');
        if (response.status === 401 && refresh_token) {
            fetch('/api/token', {
                method: 'POST',
                headers: {
                    Authorization: 'Bearer ' + localStorage.getItem('access_token'),
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    refreshToken: getCookie('refresh_token'),
                }),
            })
                .then(res => {
                    if (res.ok) {
                        return res.json();
                    }
                })
                .then(result => { // 재발급이 성공하면 로컬 스토리지값을 새로운 액세스 토큰으로 교체
                    localStorage.setItem('access_token', result.accessToken);
                    httpRequest(method, url, body, success, fail);
                })
                .catch(error => fail());
        } else {
            return fail();
        }
    });
}