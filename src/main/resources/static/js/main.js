const inputField = document.getElementById('search-input');
const parentDiv = inputField.parentElement;

inputField.addEventListener('focus', function () {
    parentDiv.classList.add('focused');
});

inputField.addEventListener('blur', function () {
    parentDiv.classList.remove('focused');
});

$(document).ready(() => {
    getLatestArticles();
})

const getLatestArticles = async () => {
    const success = async (response) => {
        const json = await response.json();
        const latestArticles = json.articles;
        console.log(latestArticles);

        let latestArticlesHTML = '';
        for (const item of Object.entries(latestArticles)) {
            latestArticlesHTML += latestArticlesTemplate(item);
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

const searchButton = document.getElementById('search-button');

searchButton.addEventListener('click', event => {
    $("#search-input").css("display", "block");
    $("#search-input").focus();
});
