const categoriesTemplate = (categories) => {
    const template = categories.categories.map(category => `
        <li class="list-group-item" data-active="${category.id}">
            <div class="d-flex flex-row">
                <a class="p-3 lh-sm w-100 text-decoration-none" 
                    onclick="loadMenusTemplate(${category.id})">
                    <div class="d-flex align-items-center">
                      <strong class="mb-1">${!category.emoji || category.emoji.length === 0 ? '' : category.emoji}${category.name}</strong>
                      <small class="ms-2">${category.updatedAt}</small>
                    </div>
                </a>
                <div class="btn-group" role="group" aria-label="First group">
                    <button type="button" class="btn btn-light" data-bs-toggle="modal" data-bs-target="#categoryUpdateDetail" onclick="showCategoryUpdateModal(${category.id})">
                        <i class="bi bi-pencil"></i>
                    </button>
                    <button type="button" class="btn btn-light" onclick="sendDeleteCategoryRequest(${category.id})">
                        <i class="bi bi-trash3-fill"></i>
                    </button>
                </div>
            </div>
        </li>
    `).join("");

    return template;
}

const menusTemplate = (menus) => {
    // 테이블 바디 생성 (반복문으로 JSON 데이터 추가)
    const template = menus.menus.map(menu => `
        <li class="list-group-item list-group-item-action">
            <div class="d-flex flex-row">
                <a onclick="loadMenuDetailTemplate(${menu.id})" aria-current="true" class="p-3 d-flex w-100 align-items-center
                 mx-2 text-decoration-none link-dark"
                    data-bs-toggle="modal" data-bs-target="#menuDetailModal">    
                    <strong class="mb-1">${menu.name}</strong>
                    <small class="ms-2">${menu.updatedAt}</small>
                </a>
                <button type="button" class="btn btn-light" onclick="deleteMenu(${menu.id})">
                    <i class="bi bi-trash3-fill"></i>
                </button>
            </div>
        </li>
    `).join("");

    // 최종 테이블 조합
    return template;
}

const menuDetailModalTemplate = (menu) => {
    const template = `
            <div class="modal-header">
                <h1 class="modal-title fs-5" id="exampleModalLabel">메뉴 정보</h1>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div>
                    <label>카테고리</label>
                    ${menu.categoryEmoji} ${menu.categoryName}
                </div>
                <div>
                    <label>이름</label>
                    ${menu.emoji} ${menu.name}
                </div>
                <div>
                    <label>설명</label>
                    ${menu.description}
                </div>
            </div>
            <div class="modal-footer">
                <button id="menuUpdateBtn" type="button" class="btn btn-primary">수정</button>
            </div>
    `
    return template
}

const menuDetailUpdateTemplate = (menu, categories) => {
    const selectOptions = categories.categories.map(category => `
        <option value="${category.id}" ${menu.categoryId == category.id ? "selected" : ""}>${category.name}</option>
    `)
    const categoriesSelectTemplate = `
        <select class="form-select" name="categoryId" aria-label="Default select categories">
          ${selectOptions}
        </select>
    `

    const template = `
        <div class="modal-header">
            <h1 class="modal-title fs-5" id="menuUpdateLabel">메뉴 정보</h1>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div id="menuUpdateContent" class="modal-body">
            <form id="menuUpdateForm">
                <input name="id" type="hidden" value="${menu.id}"/>
                <input name="categoryId" type="hidden" value="${menu.categoryId}"/>
                <div class="mb-3">
                    <label for="category-name" class="col-form-label">카테고리 : </label>
                    ${categoriesSelectTemplate}
                </div>
                <div class="mb-3">
                    <label for="menu-name" class="col-form-label">이름 : </label>
                    <input name="name" type="text" class="form-control" id="menu-name" value="${menu.name}"/>
                </div>
                <div class="mb-3">
                    <label for="menu-description" class="col-form-label">설명 : </label>
                    <input name="description" type="text" class="form-control" id="menu-description" value="${menu.description}"/>
                </div>
            </form>

        </div>
        <div class="modal-footer">
            <button onclick="updateMenu()" type="button" class="btn btn-primary">수정</button>
        </div>
    `
    return template;
}


const categoryDetailUpdateTemplate = async (category) => {
    const template = `
        <div class="modal-header">
            <h1 class="modal-title fs-5" id="menuUpdateLabel">카테고리 정보</h1>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div id="categoryUpdateContent" class="modal-body">
            <form id="categoryModifyForm">
                <input name="id" type="hidden" value="${category.id}"/>
                <div class="mb-3">
                    <label for="category-name" class="col-form-label">이름 : </label>
                    <input name="name" type="text" class="form-control" id="category-name" value="${category.name}"/>
                </div>
            </form>
        </div>
        <div class="modal-footer">
            <button onclick="sendModifyCategoryRequest()" type="button" class="btn btn-primary">수정</button>
        </div>
    `
    return template;
}

const articlesTemplate = (articles) => {
    if (!articles || articles.length === 0) {
        return `
            <tr>
                <td colspan="7" class="text-center">조회된 데이터가 없습니다</td>
            </tr>
        `;
    }

    const template = articles.map(article =>
        `
            <tr>
                <td style="width: 0px">
                    <div class="form-check d-none article-checkbox-container">
                        <input class="form-check-input article-checkbox" type="checkbox" 
                               data-article-id="${article.id}" id="tableCheckBox">
                        <label class="form-check-label" for="tableCheckBox"></label>
                    </div>
                </td>
                <td>
                    ${article.menuName}
                </td>
                <td>
                    <div class="d-flex align-items-center">
                        <div class="">
                            <div>${article.title}</div>
                        </div>
                    </div>
                </td>
                <td>
                    <div class="fs-sm text-body-secondary">${article.subTitle}</div>
                </td>
                <td>
                    ${article.author}
                </td>
                <td>
                    <div class="fs-sm text-body-secondary">${article.createdAt}</div>
                </td>
                <td>
                    <div class="fs-sm text-body-secondary">${article.updatedAt}</div>
                </td>
            </tr>
        `
    ).join("");
    return template;
}