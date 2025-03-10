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

const updateCategory = async (categoryFormData) => {
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