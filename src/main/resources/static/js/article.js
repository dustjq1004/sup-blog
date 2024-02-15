// 삭제 기능
const deleteButton = document.getElementById('delete-btn');

if (deleteButton) {
    deleteButton.addEventListener('click', event => {
        const id = document.getElementById('article-id').value;

        function success() {
            alert('삭제가 완료되었습니다.');
            location.replace('/blog');
        }

        function fail() {
            alert('삭제 실패했습니다.');
            location.replace('/blog');
        }

        const options = {
            method: 'DELETE'
        };

        httpRequest(`/api/articles/${id}`, options, success, fail);
    });
}

// 수정 기능
const modifyButton = document.getElementById('modify-btn');

if (modifyButton) {
    modifyButton.addEventListener('click', event => {
        let params = new URLSearchParams(location.search);
        let id = params.get('id');
        const content = easyMDE.value();
        const selectElement = document.getElementById('menu');
        const selectedMenu = selectElement.options[selectElement.selectedIndex];
        const menuName = selectedMenu.text;
        body = JSON.stringify({
            title: document.getElementById('title').value,
            content: content,
            menuId: selectElement.value
        });

        function success() {
            alert('수정 완료되었습니다.');
            location.replace(`/blog/${menuName}/${id}`);
        }

        function fail() {
            alert('수정 실패했습니다.');
            location.replace(`/blog/${menuName}/${id}`);
        }

        const options = {
            method: 'PUT',
            body: body
        };

        httpRequest(`/api/articles/${id}`, options, success, fail);
    });
}

// 생성 기능
const createButton = document.getElementById('create-btn');

if (createButton) {
    // 등록 버튼을 클릭하면 /api/articles로 요청을 보낸다
    createButton.addEventListener('click', event => {
        const content = easyMDE.value();
        console.log(content);
        const body = JSON.stringify({
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

        const options = {
            method: 'POST',
            body: body
        };

        httpRequest('/api/articles', options, success, fail)
    });
}

