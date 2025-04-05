const getCategoriesFragment = async (element) => {
    let fragment = ""
    const options = {
        method: 'GET'
    }

    async function success(response) {
        fragment = await response.text()
    }

    function fail(json) {
    }

    await httpRequest('/admin/frag/categories', options, success, fail)

    return fragment
}

const getCategoryById = async (categoryId) => {
    let category = {}
    const options = {
        method: 'GET'
    }

    async function success(response) {
        category = await response.json()
    }


    function fail(json) {
    }

    await httpRequest(`/api/category?categoryId=${categoryId}`, options, success, fail)
    return category;
}

const getAllCategory = async () => {
    let categories = {}

    const options = {
        method: 'GET'
    }

    async function success(response) {
        categories = await response.json()
    }

    function fail(json) {
    }

    await httpRequest('/api/categories', options, success, fail)

    return categories;
}

const createCategory = async (categoryFormData) => {

    const options = {
        method: 'POST',
        body: JSON.stringify(categoryFormData)
    }

    async function success(response) {
        alert("카테고리 정보가 추가 됐습니다.")
    }

    function fail(json) {
    }

    await httpRequest(`/api/category`, options, success, fail)
}

const putCategoryUpdate = async (categoryFormData) => {
    const options = {
        method: 'PUT',
        body: JSON.stringify(categoryFormData)
    }

    async function success(response) {
        alert("카테고리 정보가 수정 됐습니다.")
        const jsonData = await response.json()
    }

    function fail(json) {
    }

    await httpRequest(`/api/category/update`, options, success, fail)
}

const deleteCategory = async (categoryId) => {
    const options = {
        method: 'DELETE',
        body: JSON.stringify({"categoryId": categoryId})
    }

    async function success(response) {
        alert("카테고리가 삭제 됐습니다.")
    }


    function fail(response) {
        alert("카테고리 삭제에 실패했습니다.")
    }

    await httpRequest(`/api/category/delete`, options, success, fail)
}

/*
*
* Menu Transaction
*
*/
const getMenus = async (categoryId) => {
    let menus = {}
    const options = {
        method: 'GET'
    };

    function getSuccess() {
        return async (response) => {
            menus = await response.json()
        }
    }

    function fail(json) {
    }

    await httpRequest(`/api/menus/${categoryId}`, options, getSuccess(), fail)
    return menus
}

const getMenuDetailById = async (menuId) => {
    let menu = {}
    const options = {
        method: 'GET'
    };

    function getSuccess() {
        return async (response) => {
            menu = await response.json();

        }
    }

    function fail(json) {
    }

    await httpRequest(`/api/menu/${menuId}`, options, getSuccess(), fail)
    return menu;
}

const putMenuUpdate = async (menuFormData) => {
    const options = {
        method: 'PUT',
        body: JSON.stringify(menuFormData)
    }

    async function success(response) {
        alert("메뉴 정보가 수정 됐습니다.")

        const jsonData = await response.json()
    }

    function fail(json) {
    }

    await httpRequest(`/api/menu/update`, options, success, fail)
}

const deleteMenuRemove = async (menuId) => {
    const options = {
        method: 'DELETE',
        body: JSON.stringify({"menuId": menuId})
    }

    async function success(response) {
        alert("메뉴가 삭제 됐습니다.")

        const jsonData = await response.json()
        loadMenusTemplate(jsonData.categoryId)
    }


    function fail(json) {
        alert("메뉴를 삭제할 수 없습니다.")
    }

    httpRequest(`/api/menu/delete`, options, success, fail)
}

const postMenuAdd = async (menuFormData) => {
    const options = {
        method: 'POST',
        body: JSON.stringify(menuFormData)
    }

    async function success(response) {
        alert("메뉴 정보가 추가 됐습니다.")
        const jsonData = await response.json()
        loadMenusTemplate(jsonData.categoryId)
    }

    function fail(json) {
    }

    await httpRequest(`/api/menu`, options, success, fail)
}


const getArticlesFragment = async () => {
    let fragment = {}
    const options = {
        method: 'GET'
    }

    async function success(response) {
        fragment = await response.text()
    }

    function fail(json) {
    }

    await httpRequest('/admin/frag/articles', options, success, fail)

    return fragment
}

const getArticles = async (menuName, pageNumber) => {
    let articles = {}
    const options = {
        method: 'GET'
    }

    const queryParams = {}
    if (!isNull(menuName)) {
        queryParams.menuName = menuName
    }
    queryParams.pageNumber = pageNumber

    const queryString = new URLSearchParams(queryParams).toString()

    async function success(response) {
        const result = await response.json()
        articles = result
    }

    function fail(json) {
    }

    await httpRequest(`/api/articles?${queryString}`, options, success, fail)

    return articles
}