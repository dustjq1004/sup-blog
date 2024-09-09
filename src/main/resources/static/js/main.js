const inputField = document.getElementById('search-input');
const parentDiv = inputField.parentElement;

inputField.addEventListener('focus', function () {
    parentDiv.classList.add('focused');
})

inputField.addEventListener('blur', function () {
    parentDiv.classList.remove('focused');
    // $(".dropdown-menu").hide();
})

inputField.addEventListener('keyup', (e) => {
    getSearchedArticles(e.target.value);
    $(".dropdown-menu").show();
})

$(document).ready(() => {
    getLatestArticles();
})

const getLatestArticles = async () => {
    const success = async (response) => {
        const json = await response.json();
        const latestArticles = json.articles;

        let latestArticlesHTML = '';
        for (const item of Object.entries(latestArticles)) {
            latestArticlesHTML += latestArticlesTemplate(item[1]);
        }
        $("#article-list").html(latestArticlesHTML);
    }

    const fail = async (data) => {

    }

    const options = {
        method: "GET"
    }
    httpRequest('/api/main/articles/latest', options, success, fail)
}

const getSearchedArticles = async (searchParam) => {
    const success = async (response) => {
        const json = await response.json();
        const searchedArticles = json.articles;
        let searchedArticlesHtml = '';
        for (const article of Object.entries(searchedArticles)) {
            searchedArticlesHtml += searchedArticlesTemplate(article[1]);
        }

        $("#searched-list").html(searchedArticlesHtml);
    }

    const fail = async (data) => {

    }

    const options = {
        method: "GET"
    }

    const params = {
        "titleParam": searchParam
    }

    const queryString = new URLSearchParams(params).toString();


    httpRequest(`/api/main/articles/search?${queryString}`, options, success, fail)
}
