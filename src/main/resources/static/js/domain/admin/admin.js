let currentCategoryId = "";

/* 카테고리 Function */
const loadCategoriesTemplate = async (element) => {
    const fragment = await getCategoriesFragment(element)
    $('#admin-content').html(fragment)

    await setCategoriesTemplate();

    $('#category-add-id').val("")
    $('#category-add-name').val("")
}

const setCategoriesTemplate = async () => {
    const categories = await getAllCategory()
    const template = await categoriesTemplate(categories)
    $('#categories-content').html(template)
}

const showCategoryUpdateModal = async (categoryId) => {
    const category = await getCategoryById(categoryId);
    const template = await categoryDetailUpdateTemplate(category)
    $('#category-update-modal').html(template)
}

const saveAndRenderCategory = async () => {
    const categoryFormData = $('#categoryAddForm').serializeFormToJSON()
    await createCategory(categoryFormData)
    await setCategoriesTemplate()

    bootstrap.Modal.getInstance($('#categoryAddDetail')).hide()
    $('.modal-backdrop').remove()

    $("#categoryAddForm")[0].reset()
}

const sendModifyCategoryRequest = async () => {
    const categoryFormData = $('#categoryModifyForm').serializeFormToJSON()
    await putCategoryUpdate(categoryFormData)
    const categories = await getAllCategory()

    const template = categoriesTemplate(categories)
    $('#categories-content').html(template)

    $('.modal-backdrop').remove();
    bootstrap.Modal.getInstance($('#categoryUpdateDetail')).hide()
}

const sendDeleteCategoryRequest = async (categoryId) => {
    if (!confirm("카테고리를 삭제하시겠습니까?")) {
        return
    }

    await deleteCategory(categoryId)

    const categories = await getAllCategory()
    const template = await categoriesTemplate(categories)
    $('#categories-content').html(template)
}

/* 카테고리 Function */

const loadMenusTemplate = async (categoryId) => {
    const menus = await getMenus(categoryId)

    const template = await menusTemplate(menus)
    $('#menus-content').html(template)

    const category = await getCategoryById(categoryId);
    $('#category-add-id').val(categoryId)
    $('#category-add-name').val(category.emoji + category.name);
}


const loadMenuDetailTemplate = async (menuId) => {
    const menu = await getMenuDetailById(menuId)
    const template = menuDetailModalTemplate(menu)
    $('#menu-detail-modal').html(template)
    initializeUpdateModalEvent(menu)
}

const updateMenu = async () => {
    const menuFormData = $('#menuUpdateForm').serializeFormToJSON()
    await putMenuUpdate(menuFormData)
    await loadMenusTemplate(currentCategoryId)

    bootstrap.Modal.getInstance($('#menuUpdateDetail')).hide()
}

const deleteMenu = async (menuId) => {
    if (!confirm("메뉴를 삭제하시겠습니까?")) {
        return
    }

    await deleteMenuRemove(menuId)
}

const initializeUpdateModalEvent = (menu) => {
    document.getElementById('menuUpdateBtn').addEventListener('click', async function () {
        const categories = await getAllCategory()
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
}

const sendSaveMenuRequest = async () => {
    const menuFormData = $('#menuAddForm').serializeFormToJSON();

    if (!verifyMenuInput(menuFormData)) {
        return false;
    }

    await postMenuAdd(menuFormData)
    bootstrap.Modal.getInstance($('#menuAddDetail')).hide()
    $('#menuAddForm')[0].reset()
}

function verifyMenuInput(jsonFormData) {
    const categoryId = jsonFormData['categoryId'];
    if (categoryId == null || categoryId == 'undefined') {
        alert("카테고리를 선택해주세요.");
        return false;
    }

    return true;
}

/* Article Transaction */
const loadArticlesFragment = async () => {
    $('#admin-content').html("")
    const fragment = await getArticlesFragment()
    $('#admin-content').html(fragment)

    const articles = await getArticles()
    console.log(articles)
    const template = await articlesTemplate(articles)

    $('#article-tbody').html(template)
}

/* document.ready  */
$(document).ready(function () {
    $(document).on("click", ".admin-nav", (event) => {
        $(".admin-nav").each((index, element) => {
            $(element).removeClass("active")
        })
        $(event.currentTarget).addClass("active")
    })

    $(document).on("click", "#categories-content .list-group-item", (event) => {
        $("#categories-content .list-group-item").removeClass("active") // 모든 항목에서 active 제거
        $(event.currentTarget).addClass("active")  // 클릭한 항목에 active 추가
        currentCategoryId = $(event.currentTarget).data("active")
    })
})

