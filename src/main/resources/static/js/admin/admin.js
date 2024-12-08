const callGetCategories = async () => {
    const options = {
        method: 'GET'
    };

    function success(response) {

    };

    function fail(json) {
    }

    httpRequest('/categoryies', options, success, fail)
}

const callGetMenus = async () => {
    const options = {
        method: 'GET'
    };

    function success(response) {

    };

    function fail(json) {
    }

    httpRequest('/menus', options,
        async (response) => {
            
        },
        fail)
}