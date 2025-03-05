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

const getCategoryUpdateModal = async (categoryId) => {
    let category = "";
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