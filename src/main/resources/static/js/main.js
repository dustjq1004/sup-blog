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
        console.log(await response.json());
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
