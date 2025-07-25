let draftList = {};

function setDraftList(json) {
    let html = "";
    let index = 0;
    if (json.length <= 0) return html = `<li class="draft-item"><div class="draft-no-item-box"><span class="dropdown-no-item-text">임시로 저장된 게시글이 없습니다.</span></div></li>`

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
                        <span class="draft-item-title" data-bs-toggle="tooltip" data-bs-placement="top" data-bs-custom-class="custom-tooltip" title="${draft.title}">
                            ${draft.title}
                        </span>
                    </div>
                    <div class="draft-item-action fs-3">
                        <!-- 업로드 아이콘 -->
                        <svg xmlns="http://www.w3.org/2000/svg" width="20" height="50" fill="currentColor" class="bi bi-upload" viewBox="0 0 16 16">
                            <path d="M.5 9.9a.5.5 0 0 1 .5.5v2.5a1 1 0 0 0 1 1h12a1 1 0 0 0 1-1v-2.5a.5.5 0 0 1 1 0v2.5a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2v-2.5a.5.5 0 0 1 .5-.5"/>
                            <path d="M7.646 1.146a.5.5 0 0 1 .708 0l3 3a.5.5 0 0 1-.708.708L8.5 2.707V11.5a.5.5 0 0 1-1 0V2.707L5.354 4.854a.5.5 0 1 1-.708-.708z"/>
                        </svg>
                        <!-- 삭제 버튼(아이콘) 추가 -->
                        <button type="button" class="btn ms-2 draft-delete-btn" onclick="deleteDraft(${draft.id}, event)">
                            <i class="bi bi-trash3"></i>
                        </button>
                    </div>
                </div>
            </a>
        </li>
        `
    }
    return html;
}

function loadDraft(index, event) {
    const draft = draftList[index];
    if (!confirm(`"${draft.title}" \n을 불러오시겠습니까?`)) {
        return false;
    }

    resetForm();

    $('#draft-id').val(draft.id);
    $('#menu').val(draft.menuId);
    $('#title').val(draft.title);
    $('#subTitle').val(draft.subTitle);
    easyMDE.value(draft.content);
    bootstrap.Modal.getInstance($('#draftModal')).hide()
}

async function deleteDraft(draftId, event) {
    event.stopPropagation(); // 부모 a 태그 클릭 방지
    if (!confirm('이 임시글을 삭제하시겠습니까?')) return;


    const options = {
        method: 'DELETE'
    };

    await httpRequest(`/api/draft/${draftId}`, options, () => {
        getDrafts();
    }, () => {
    })

}

function resetForm() {
    $('form').each(function () {
        this.reset();
    });
    $('#draft-id').val("");
    easyMDE.value("");
}

async function getDrafts() {
    const options = {
        method: 'GET'
    };

    const success = async (body) => {
        draftList = await body.json();
        html = setDraftList(draftList);

        $('#draft-list').html(html);
        isDraftSaved = false;

        const tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]')
        const tooltipList = [...tooltipTriggerList].map(tooltipTriggerEl => new bootstrap.Tooltip(tooltipTriggerEl))
    }

    await httpRequest('/api/draft', options, success, () => {
    })
}

$(document).ready(function () {
    $("#draft-button").click(async function () {
        await getDrafts();
    })

    $("form :input").on("input", function () {
        isDraftSaved = true;
    });

    $(window).bind('beforeunload', function () {
        if (isDraftSaved)
            return '페이지를 나가시겠습니까? 기존에 작성한 글은 복구할 수 없습니다.';
    });
});
