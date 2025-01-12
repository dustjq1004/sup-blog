const callGetCategories = async () => {
    const options = {
        method: 'GET'
    };

    async function success(response) {
        const items = await response.json()
        // 대상 요소 선택
        const template = categoriesTemplate(items);
        $('#categories-content').html(template);
    };


    function fail(json) {
    }

    httpRequest('/api/categories', options, success, fail)
}

const callGetMenus = async (categoryId) => {
    const options = {
        method: 'GET'
    };

    function fail(json) {
    }

    httpRequest(`/api/menus/${categoryId}`, options,
        async (response) => {
            const item = await response.json();
            const template = menusTemplate(item);
            $('#menus-content').html(template);
        },
        fail)
}

const loadCategoriesFragment = () => {
    const options = {
        method: 'GET'
    };

    async function success(response) {
        const fragment = await response.text();
        $('#admin-content').html(fragment);

        callGetCategories();
    };


    function fail(json) {
    }

    httpRequest('/admin/frag/categories', options, success, fail)
}