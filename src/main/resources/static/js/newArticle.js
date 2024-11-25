let draftList = {};

function setDraftList(json) {
    let html = "";
    let index = 0;
    console.log(json)
    if (json.length <= 0) return html = `<li><span class="dropdown-item-text">임시로 저장된 게시글이 없습니다.</span></li>`

    for (let draft of json) {
        html +=
            `
        <li class="draft-item">
            <a class="" onclick="loadDraft(${index++})">
                <div class="draft-item-container">
                    <div class="draft-item-icon">
                        <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" class="bi bi-file-earmark-text" viewBox="0 0 16 16">
                          <path d="M5.5 7a.5.5 0 0 0 0 1h5a.5.5 0 0 0 0-1zM5 9.5a.5.5 0 0 1 .5-.5h5a.5.5 0 0 1 0 1h-5a.5.5 0 0 1-.5-.5m0 2a.5.5 0 0 1 .5-.5h2a.5.5 0 0 1 0 1h-2a.5.5 0 0 1-.5-.5"/>
                          <path d="M9.5 0H4a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h8a2 2 0 0 0 2-2V4.5zm0 1v2A1.5 1.5 0 0 0 11 4.5h2V14a1 1 0 0 1-1 1H4a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1z"/>
                        </svg>
                    </div>
                    <div class="draft-item-wrapper">
                        <span class="draft-item-title">
                            ${draft.title}
                        </span>
                    </div>
                    <div class="draft-item-action fs-3">
                        <svg xmlns="http://www.w3.org/2000/svg" width="20" height="50" fill="currentColor" class="bi bi-upload" viewBox="0 0 16 16">
                          <path d="M.5 9.9a.5.5 0 0 1 .5.5v2.5a1 1 0 0 0 1 1h12a1 1 0 0 0 1-1v-2.5a.5.5 0 0 1 1 0v2.5a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2v-2.5a.5.5 0 0 1 .5-.5"/>
                          <path d="M7.646 1.146a.5.5 0 0 1 .708 0l3 3a.5.5 0 0 1-.708.708L8.5 2.707V11.5a.5.5 0 0 1-1 0V2.707L5.354 4.854a.5.5 0 1 1-.708-.708z"/>
                        </svg>
                    </div>
                </div>
            </a>
        </li>
        `
    }
    return html;
}

function loadDraft(index) {
    const draft = draftList[index];
    if (!confirm(`임시저장한 게시글 ${draft.title}을 불러오시겠습니까?`)) return

    resetForm();

    $('#draft-id').val(draft.id);
    $('#menu').val(draft.menuId);
    $('#title').val(draft.title);
    $('#subTitle').val(draft.subTitle);
    easyMDE.value(draft.content);
}

function resetForm() {
    $('form').each(function () {
        this.reset();
    });
    $('#draft-id').val("");
    easyMDE.value("");
}

const success = async (body) => {
    draftList = await body.json();
    html = setDraftList(draftList);

    $('#draft-list').html(html);
    isDraftSaved = false;
}

$(document).ready(function () {
    $("#draft-button").click(async function () {
        const options = {
            method: 'GET'
        };

        await httpRequest('/api/draft', options, success, () => {
        })
    })

    $("form :input").on("input", function () {
        isDraftSaved = true;
    });

    $(window).bind('beforeunload', function () {
        if (isDraftSaved)
            return '페이지를 나가시겠습니까? 기존에 작성한 글은 복구할 수 없습니다.';
    });
});
