let draftList = {};

function setDraftList(json) {
    let html = "";
    let index = 0;

    if (json.length <= 0) return html = `<li><span class="dropdown-item-text">임시로 저장된 게시글이 없습니다.</span></li>`

    for (let draft of json) {
        html += `<li><a class="dropdown-item" onclick="loadDraft(${index++})">${draft.title}</a></li>`
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

const success = (json) => {
    draftList = json;
    html = setDraftList(json);

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
