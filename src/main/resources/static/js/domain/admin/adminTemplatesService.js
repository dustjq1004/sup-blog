function getCategoriesTemplate(categories) {
    // 대상 요소 선택
    const template = categoriesTemplate(categories)
    $('#categories-content').html(template)

    $("#categories-content .list-group-item").click(function () {
        $("#categories-content .list-group-item").removeClass("active"); // 모든 항목에서 active 제거
        $(this).addClass("active"); // 클릭한 항목에 active 추가
    });

    $('#category-add-id').val("")
    $('#category-add-name').val("")
}