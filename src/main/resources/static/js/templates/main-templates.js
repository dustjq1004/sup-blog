const latestArticlesTemplate = (item) => {
    const thumbnailUrl = item.thumbnailUrl ? item.thumbnailUrl : '/img/noimage01.png';
    const updatedAt = dayjs(item.updatedAt);
    console.log(item);
    return `
    <div class="col">
        <a href="/blog/${item.menuName}/${item.id}"
           class="link-offset-2 link-underline link-underline-opacity-0">
            <div class="card border border-0 p-2 rounded-4">
                <svg class="bd-placeholder-img card-img-top rounded-4" width="100%" height="180"
                     xmlns="http://www.w3.org/2000/svg" role="img"
                     preserveAspectRatio="xMidYMid slice" focusable="false"><title>Placeholder</title>
                    <image height="100%" width="100%"
                           preserveAspectRatio="none"
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
