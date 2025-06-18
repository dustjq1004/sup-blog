let currentCategoryId = "";

// 편집 모드 상태 관리
let isEditMode = false;

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

    const modal = new bootstrap.Modal($('#menuAddDetail'))
    modal.show()
}

const sendSaveMenuRequest = async () => {
    const menuFormData = $('#menuAddForm').serializeFormToJSON()

    if (!verifyMenuInput(menuFormData)) {
        return false;
    }

    await postMenuAdd(menuFormData)
    bootstrap.Modal.getInstance($('#menuAddDetail')).hide()
    $('#menuAddForm')[0].reset()
}

function verifyMenuInput(jsonFormData) {
    const categoryId = jsonFormData['categoryId']
    if (categoryId == null || categoryId == 'undefined') {
        alert("카테고리를 선택해주세요.")
        return false;
    }

    return true;
}

/* Article Transaction */
const loadArticlesFragment = async (pageNumber) => {
    $('#admin-content').html("")
    const fragment = await getArticlesFragment()
    $('#admin-content').html(fragment)

    // 초기 카테고리 데이터 로드
    loadCategories();

    await loadArticles(pageNumber)
}

async function loadArticles(pageNumber) {
    const maxPageNumber = 10
    const categoryId = $("#filterCategory").val();
    const menuId = $("#filterMenu").val();
    const sortBy = $("#sortBy").val()
    const sortDirection = $("#sortDirection").val()
    const articleSearchFormData = $('#articleSearchForm').serializeFormToJSON()

    articleSearchFormData.categoryId = categoryId;
    articleSearchFormData.menuId = menuId;
    articleSearchFormData.sortBy = sortBy
    articleSearchFormData.direction = sortDirection

    console.log(articleSearchFormData)

    const articles = await getArticles(articleSearchFormData, pageNumber)
    const template = await articlesTemplate(articles.data)
    const pageNumberTemplate = await articlesPageNumberTemplate(articles.pageNumber, maxPageNumber, articles.totalPages)
    const pageSummary = getPageSummary(articles)

    // 페이징 시 편집모드 해제
    if (isEditMode) {
        isEditMode = false;
        updateButtonVisibility();
        hideCheckboxes();
        uncheckAllCheckboxes();
        updateSelectedCount();
    }

    $('#articlePagination').html(pageNumberTemplate)
    $('#articlePageSummary').html(pageSummary)
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

    $(document).on("click", "#adminArticlesSearch", () => {
        loadArticles(0)
    })

    // 카테고리 선택 시 메뉴 데이터 로드
    $(document).on("change", "#filterCategory", function () {
        const categoryId = $(this).val();
        if (categoryId) {
            loadMenus(categoryId);
            $("#filterMenu").prop("disabled", false);
        } else {
            $("#filterMenu").prop("disabled", true);
            $("#filterMenu").html('<option value="">Select Menu</option>');
        }
    });

    // 필터 적용 버튼 클릭
    $(document).on("click", "#applyFilter", () => {
        loadArticles(0);

        // 드롭다운 닫기
        const dropdownElement = document.getElementById('articleFilterButton');
        const dropdown = bootstrap.Dropdown.getInstance(dropdownElement);
        if (dropdown) {
            dropdown.hide();
        }
    });

    // Sort 적용 버튼 클릭
    $(document).on("click", "#applySort", () => {
        const sortBy = $("#sortBy").val();
        const sortDirection = $("#sortDirection").val();
        const articleSearchFormData = $('#articleSearchForm').serializeFormToJSON();
        articleSearchFormData.sortBy = sortBy;
        articleSearchFormData.direction = sortDirection;
        loadArticles(0);

        // 드롭다운 닫기
        const dropdownElement = document.getElementById('articleSortButton');
        const dropdown = bootstrap.Dropdown.getInstance(dropdownElement);
        if (dropdown) {
            dropdown.hide();
        }
    });

    // 편집 버튼 클릭 이벤트
    $(document).on("click", "#editButton", function () {
        isEditMode = true;
        updateButtonVisibility();
        showCheckboxes();
    });

    // 취소 버튼 클릭 이벤트
    $(document).on("click", "#cancelButton", function () {
        isEditMode = false;
        updateButtonVisibility();
        hideCheckboxes();
        uncheckAllCheckboxes();
    });

    // 삭제 버튼 클릭 이벤트
    $(document).on('click', "#deleteButton", function () {
        const selectedIds = getSelectedArticleIds();
        if (selectedIds.length === 0) {
            alert('삭제할 항목을 선택해주세요.');
            return;
        }

        if (confirm('선택한 항목을 삭제하시겠습니까?')) {
            deleteSelectedArticles(selectedIds);
        }
    });

    // 전체 선택 체크박스 이벤트
    $(document).on('change', "#tableCheckAll", function () {
        const isChecked = $(this).prop('checked');
        $('.article-checkbox').prop('checked', isChecked);
    });

    // 체크박스 선택 이벤트 추가
    $(document).on('change', '.article-checkbox', function () {
        updateSelectedCount();
    });

    // 편집 버튼 클릭 이벤트 수정
    $(document).on('click', '#editButton', function () {
        isEditMode = true;
        updateButtonVisibility();
        showCheckboxes();
        updateSelectedCount(); // 초기 상태 업데이트
    });

    // 취소 버튼 클릭 이벤트 수정
    $(document).on('click', '#cancelButton', function () {
        isEditMode = false;
        updateButtonVisibility();
        hideCheckboxes();
        uncheckAllCheckboxes();
        updateSelectedCount();
    });

    // 전체 선택 체크박스 이벤트 수정
    $(document).on('change', '#tableCheckAll', function () {
        const isChecked = $(this).prop('checked');
        $('.article-checkbox').prop('checked', isChecked);
        updateSelectedCount();
    });
})

// 카테고리 데이터 로드
const loadCategories = async () => {
    const options = {
        method: 'GET'
    };

    async function success(response) {
        const json = await response.json();
        let options = '<option value="">Select Category</option>';
        json.categories.forEach(category => {
            options += `<option value="${category.id}">${category.name}</option>`;
        });
        $("#filterCategory").html(options);
    }

    function fail(error) {
        console.error('Failed to load categories:', error);
    }

    await httpRequest('/api/categories', options, success, fail);
}

// 메뉴 데이터 로드
const loadMenus = async (categoryId) => {
    const options = {
        method: 'GET'
    };

    async function success(response) {
        const json = await response.json();
        let options = '<option value="">Select Menu</option>';
        json.menus.forEach(menu => {
            options += `<option value="${menu.id}">${menu.name}</option>`;
        });
        $("#filterMenu").html(options);
    }

    function fail(error) {
        console.error('Failed to load menus:', error);
    }

    await httpRequest(`/api/menus/${categoryId}`, options, success, fail);
}

// 버튼 가시성 업데이트
function updateButtonVisibility() {
    if (isEditMode) {
        $('#editButton').addClass('d-none');
        $('#actionButtons').removeClass('d-none');
    } else {
        $('#editButton').removeClass('d-none');
        $('#actionButtons').addClass('d-none');
    }
}

// 체크박스 표시
function showCheckboxes() {
    $('#tableCheckAllContainer').removeClass('d-none');
    $('.article-checkbox-container').removeClass('d-none');
}

// 체크박스 숨김
function hideCheckboxes() {
    $('#tableCheckAllContainer').addClass('d-none');
    $('.article-checkbox-container').addClass('d-none');
}

// 모든 체크박스 해제
function uncheckAllCheckboxes() {
    $('#tableCheckAll').prop('checked', false);
    $('.article-checkbox').prop('checked', false);
}

// 선택된 게시글 ID 가져오기
function getSelectedArticleIds() {
    return $('.article-checkbox:checked').map(function () {
        return $(this).data('article-id');
    }).get();
}

// 선택된 항목 수 업데이트
function updateSelectedCount() {
    const selectedCount = $('.article-checkbox:checked').length;
    const totalCount = $('.article-checkbox').length;

    if (selectedCount === 0) {
        $('#selectedCountText').text('No selected');
    } else {
        $('#selectedCountText').text(`${selectedCount} selected`);
    }
}