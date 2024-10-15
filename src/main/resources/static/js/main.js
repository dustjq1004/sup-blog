const inputField = document.getElementById('search-input');
const parentDiv = inputField.parentElement;

inputField.addEventListener('focus', function () {
    parentDiv.classList.add('focused');
})

inputField.addEventListener('blur', function () {
    parentDiv.classList.remove('focused');
})

inputField.addEventListener('keyup', (e) => {
    getSearchedArticles(e.target.value);
    $(".dropdown-menu").show();
})

$('#search-cancel-button').on('click', () => {
    $(".dropdown-menu").hide();
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

function setArticlesHtml(searchedArticles) {
    let searchedArticlesHtml = '';
    for (const article of Object.entries(searchedArticles)) {
        searchedArticlesHtml += searchedArticlesTemplate(article[1]);
    }
    return searchedArticlesHtml;
}

const getSearchedArticles = async (searchParam) => {
    const success = async (response) => {
        const json = await response.json();
        const searchedArticles = json.articles;

        if (searchedArticles.length == 0) {
            $("#searched-list").hide();
            $("#searched-list").html("");
            return
        }

        const html = await setArticlesHtml(searchedArticles);
        $("#searched-list").html(html);
        $("#no-search").hide();
        $("#searched-list").show();
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
