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