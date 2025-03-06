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

const saveCategory = async (jsonFormData) => {

    const options = {
        method: 'POST',
        body: JSON.stringify(jsonFormData)
    }

    async function success(response) {
        alert("카테고리 정보가 추가 됐습니다.")
    }

    function fail(json) {
    }

    await httpRequest(`/api/category`, options, success, fail)
}