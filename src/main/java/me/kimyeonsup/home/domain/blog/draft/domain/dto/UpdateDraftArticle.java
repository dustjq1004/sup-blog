package me.kimyeonsup.home.domain.blog.draft.domain.dto;


import lombok.Getter;
import me.kimyeonsup.home.domain.blog.draft.domain.entity.DraftArticle;
import me.kimyeonsup.home.domain.blog.menu.domain.entity.Menu;

@Getter
public class UpdateDraftArticle {

    private final Long id;
    private final String title;
    private final String subTitle;
    private final String content;
    private final Long menuId;

    public UpdateDraftArticle(Long id, String title, String subTitle, String content, Long menuId) {
        this.id = id;
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;
        this.menuId = menuId;
    }

    public DraftArticle toEntity(String author) {
        return DraftArticle.builder()
                .title(title)
                .content(content)
                .subTitle(subTitle)
                .menu(Menu.builder().id(menuId).build())
                .author(author)
                .build();
    }
}
