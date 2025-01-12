const categoriesTemplate = (categories) => {

    // 테이블 바디 생성 (반복문으로 JSON 데이터 추가)gap-3
    const tableBody = categories.categories.map(category => `
        <div class="list-group list-group-flush border-bottom scrollarea">
            <a onclick="callGetMenus(${category.id})" class="list-group-item list-group-item-action py-3 lh-sm" aria-current="true">                  
                <div class="d-flex w-100 align-items-center justify-content-between">
                  <strong class="mb-1">${!category.emoji || category.emoji.length === 0 ? '' : category.emoji}${category.name}</strong>
                  <small>${category.updatedAt}</small>
                </div>           
            </a>
        </div>
    `).join("");


    // 최종 테이블 조합
    return tableBody;
}

const menusTemplate = (menus) => {
    // 테이블 바디 생성 (반복문으로 JSON 데이터 추가)
    const tableBody = menus.menus.map(menu => `
        <div class="list-group list-group-flush border-bottom scrollarea">
            <a onclick="callMenuDetails(${menu.id})" class="list-group-item list-group-item-action py-3 lh-sm" aria-current="true">                  
                <div class="d-flex w-100 align-items-center justify-content-between">
                  <strong class="mb-1">${menu.name}</strong>
                  <small>${menu.updatedAt}</small>
                </div>           
            </a>
        </div>
    `).join("");

    // 최종 테이블 조합
    return tableBody;
}