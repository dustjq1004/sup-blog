function initializeCategoriesTemplate(categories) {
    // 대상 요소 선택
    const template = categoriesTemplate(categories)
    $('#categories-content').html(template)

    $("#categories-content .list-group-item").click(function () {
        $("#categories-content .list-group-item").removeClass("active"); // 모든 항목에서 active 제거
        $(this).addClass("active"); // 클릭한 항목에 active 추가
    });

    $('#category-add-id').val("")
    $('#category-add-name').val("")
}

/* 카테고리 Function */
const loadCategoriesFragment = (element) => {
    const options = {
        method: 'GET'
    }

    async function success(response) {
        const fragment = await response.text();
        $('#admin-content').html(fragment);

        const categories = await callGetCategories();

        initializeCategoriesTemplate(categories);
    }


    function fail(json) {
    }

    httpRequest('/admin/frag/categories', options, success, fail)
    $('.admin-nav.active').removeClass('active');
    $(element).addClass('active')
}

const showCategoryUpdateModal = (categoryId) => {
    const options = {
        method: 'GET'
    }

    async function success(response) {
        const category = await response.json();
        const template = categoryDetailUpdateTemplate(category)
        $('#category-update-modal').html(template)
    }


    function fail(json) {
    }

    httpRequest(`/api/category?categoryId=${categoryId}`, options, success, fail)
}

const callGetCategory = async (categoryId) => {
    let category = {};
    const options = {
        method: 'GET'
    }

    async function success(response) {
        category = await response.json();
    }


    function fail(json) {
    }

    await httpRequest(`/api/category?categoryId=${categoryId}`, options, success, fail)

    return category;
}

const callGetCategories = async () => {
    let categories = {};

    const options = {
        method: 'GET'
    };

    async function success(response) {
        const items = await response.json()
        categories = items;

    }


    function fail(json) {
    }

    await httpRequest('/api/categories', options, success, fail)

    return categories;
}

const sendSaveCategoryRequest = () => {
    const jsonFormData = $('#categoryAddForm').serializeFormToJSON();
    const options = {
        method: 'POST',
        body: JSON.stringify(jsonFormData)
    }

    async function success(response) {
        alert("카테고리 정보가 추가 됐습니다.")
        bootstrap.Modal.getInstance($('#categoryAddDetail')).hide()
        $('.modal-backdrop').remove();

        $("#categoryAddForm")[0].reset();

        const categories = await callGetCategories();

        initializeCategoriesTemplate(categories);
    }


    function fail(json) {
    }

    httpRequest(`/api/category`, options, success, fail)
}

const sendModifyCategoryRequest = () => {
    const jsonFormData = $('#categoryModifyForm').serializeFormToJSON();
    const options = {
        method: 'PUT',
        body: JSON.stringify(jsonFormData)
    }

    async function success(response) {
        alert("카테고리 정보가 수정 됐습니다.")
        bootstrap.Modal.getInstance($('#categoryUpdateDetail')).hide()
        $('.modal-backdrop').remove();
        const jsonData = await response.json()
        const categories = await callGetCategories();

        initializeCategoriesTemplate(categories);
    }


    function fail(json) {
    }

    httpRequest(`/api/category/update`, options, success, fail)
}

const sendDeleteCategoryRequest = (categoryId) => {
    if (!confirm("카테고리를 삭제하시겠습니까?")) {
        return
    }

    const options = {
        method: 'DELETE',
        body: JSON.stringify({"categoryId": categoryId})
    }

    async function success(response) {
        alert("카테고리가 삭제 됐습니다.")

        const categories = await callGetCategories()

        initializeCategoriesTemplate(categories)
    }


    function fail(response) {
        alert("카테고리 삭제에 실패했습니다.")
    }

    httpRequest(`/api/category/delete`, options, success, fail)
}

/* 카테고리 Function */

const callGetMenus = async (categoryId) => {
    getMenus(categoryId);
}

function getMenus(categoryId) {
    const options = {
        method: 'GET'
    };

    function getSuccess() {
        return async (response) => {
            const item = await response.json()
            const template = menusTemplate(item)
            $('#menus-content').html(template)

            const category = await callGetCategory(categoryId);
            $('#category-add-id').val(categoryId)
            $('#category-add-name').val(category.emoji + category.name);
        }
    }

    function fail(json) {
    }

    httpRequest(`/api/menus/${categoryId}`, options, getSuccess(), fail)
}

const callMenuDetails = (menuId) => {
    const options = {
        method: 'GET'
    };

    function getSuccess() {
        return async (response) => {
            const item = await response.json();
            const template = menuDetailModalTemplate(item);
            $('#menu-detail-modal').html(template);
            initializeUpdateModalEvent(item);
        }
    }

    function fail(json) {
    }

    httpRequest(`/api/menu/${menuId}`, options, getSuccess(), fail)
}

const sendUpdateMenuRequest = () => {
    const jsonFormData = $('#menuUpdateForm').serializeFormToJSON();
    const options = {
        method: 'PUT',
        body: JSON.stringify(jsonFormData)
    }

    async function success(response) {
        alert("메뉴 정보가 수정 됐습니다.")
        bootstrap.Modal.getInstance($('#menuUpdateDetail')).hide()

        const jsonData = await response.json()
        callGetMenus(jsonData.categoryId)
    }


    function fail(json) {
    }

    httpRequest(`/api/menu/update`, options, success, fail)
}

const sendDeleteMenuRequest = (menuId) => {
    if (!confirm("메뉴를 삭제하시겠습니까?")) {
        return
    }

    const options = {
        method: 'DELETE',
        body: JSON.stringify({"menuId": menuId})
    }

    async function success(response) {
        alert("메뉴가 삭제 됐습니다.")

        const jsonData = await response.json()
        callGetMenus(jsonData.categoryId)
    }


    function fail(json) {
    }

    httpRequest(`/api/menu/delete`, options, success, fail)
}

const initializeUpdateModalEvent = (menu) => {
    document.getElementById('menuUpdateBtn').addEventListener('click', async function () {
        let categories = {}

        const options = {
            method: 'GET'
        };

        await httpRequest('/api/categories', options, async (response) => {
            categories = await response.json();
        }, () => {
        });

        const template = menuDetailUpdateTemplate(menu, categories);
        $('#menu-update-modal').html(template);

        // 첫 번째 모달 닫기
        const firstModal = bootstrap.Modal.getInstance($('#menuDetailModal'))
        firstModal.hide()

        // backdrop 제거
        $('.modal-backdrop').remove();
        $('body').removeClass('modal-open'); // body의 'modal-open' 클래스 제거
        $('body').css('padding-right', ''); // 추가된 padding-right 제거

        // 두 번째 모달 열기
        const secondModal = new bootstrap.Modal($('#menuUpdateDetail'))
        secondModal.show()
    })
}

const showMenuAddModal = async () => {
    const categoryId = $('#category-add-id').val()
    if (!categoryId) {
        alert("카테고리를 선택해주세요.")
        return false
    }

    const modal = new bootstrap.Modal($('#menuAddDetail'));
    modal.show();
    // const categories = await callGetCategories()
    // const noSelect = `<option value="">카테고리를 선택해 주세요.</option>`
    // const selectOptions = categories.categories.map(category => `
    //     <option value="${category.id}">${category.name}</option>
    // `)
    //
    // $('#select-category').html(noSelect.concat(selectOptions));
}

function verifyFormInput(jsonFormData) {
    const categoryId = jsonFormData['categoryId'];
    if (categoryId == null || categoryId == 'undefined') {
        alert("카테고리를 선택해주세요.");
        return false;
    }

    return true;
}

const sendSaveMenuRequest = () => {
    const jsonFormData = $('#menuAddForm').serializeFormToJSON();

    if (!verifyFormInput(jsonFormData)) {
        return false;
    }

    const options = {
        method: 'POST',
        body: JSON.stringify(jsonFormData)
    }

    async function success(response) {
        alert("메뉴 정보가 추가 됐습니다.")
        bootstrap.Modal.getInstance($('#menuAddDetail')).hide()
        $('#menuAddForm')[0].reset();
        const jsonData = await response.json()
        callGetMenus(jsonData.categoryId)
    }


    function fail(json) {
    }

    httpRequest(`/api/menu`, options, success, fail)
}


/* document.ready  */
$(document).ready(function () {
})

