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
                <button type="button" class="btn btn-secondary" onclick="deleteMenu(${menu.id})">
                    <svg xmlns="http://www.w3.org/2000/svg" width="14" height="16" fill="currentColor" class="bi bi-trash" viewBox="0 2 16 16">
                      <path d="M5.5 5.5A.5.5 0 0 1 6 6v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5m2.5 0a.5.5 0 0 1 .5.5v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5m3 .5a.5.5 0 0 0-1 0v6a.5.5 0 0 0 1 0z"/>
                      <path d="M14.5 3a1 1 0 0 1-1 1H13v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V4h-.5a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1H6a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1h3.5a1 1 0 0 1 1 1zM4.118 4 4 4.059V13a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1V4.059L11.882 4zM2.5 3h11V2h-11z"/>
                    </svg>
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
                    <div class="form-check">
                        <input class="form-check-input" type="checkbox" id="tableCheckOne">
                        <label class="form-check-label" for="tableCheckOne"></label>
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