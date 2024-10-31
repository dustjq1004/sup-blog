const latestArticlesTemplate = (item) => {
    const thumbnailUrl = item.thumbnailUrl ? item.thumbnailUrl : '/img/noimage02.png';
    const updatedAt = dayjs(item.updatedAt);
    return `
    <div class="col">
        <a href="/blog/${item.menuName}/${item.id}"
           class="link-offset-2 link-underline link-underline-opacity-0">
            <div class="card border border-0 p-2 rounded-4">
                <svg class="bd-placeholder-img card-img-top rounded-4" width="100%" height="180"
                     xmlns="http://www.w3.org/2000/svg" role="img"
                     preserveAspectRatio="xMidYMid slice" focusable="false"><title>Placeholder</title>
                    <image height="100%" width="100%"
                           preserveAspectRatio="xMidYMid slice"
                           href="${thumbnailUrl}">
                    </image>
                </svg>
                <div class="card-body">
                    <div class="mb-2">
                                <span class="text-secondary fs-7"
                                      text="${updatedAt.format('yyyy.MM.dd')}">
                                </span>
                    </div>
                    <h5 class="mb-1 text-truncate">${item.title}</h5>
                    <p class="card-text text-body-tertiary fs-6 text-truncate">
                        ${item.subTitle}
                    </p>
                </div>
            </div>
        </a>
    </div>
    `
}

const searchedArticlesTemplate = (item) => {
    return `<li>
                <a class="dropdown-item" href="/blog/${item.menuName}/${item.id}">
                    <div class="row">
                    <div class="col">${item.title}</div>
                    <div class="col sub-title">${item.subTitle}</div>
                    <div class="arrow-right-icon col align-self-end text-end">
                        <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" class="bi bi-box-arrow-right" viewBox="0 0 20 20">
                              <path fill-rule="evenodd" d="M10 12.5a.5.5 0 0 1-.5.5h-8a.5.5 0 0 1-.5-.5v-9a.5.5 0 0 1 .5-.5h8a.5.5 0 0 1 .5.5v2a.5.5 0 0 0 1 0v-2A1.5 1.5 0 0 0 9.5 2h-8A1.5 1.5 0 0 0 0 3.5v9A1.5 1.5 0 0 0 1.5 14h8a1.5 1.5 0 0 0 1.5-1.5v-2a.5.5 0 0 0-1 0z"/>
                              <path fill-rule="evenodd" d="M15.854 8.354a.5.5 0 0 0 0-.708l-3-3a.5.5 0 0 0-.708.708L14.293 7.5H5.5a.5.5 0 0 0 0 1h8.793l-2.147 2.146a.5.5 0 0 0 .708.708z"/>
                        </svg>
                    </div>
                    </div>
                </a>
            </li>`
}
