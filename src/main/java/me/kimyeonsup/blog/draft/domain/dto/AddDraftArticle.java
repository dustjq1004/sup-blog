package me.kimyeonsup.blog.draft.domain.dto;


import lombok.Getter;
import me.kimyeonsup.blog.draft.domain.entity.DraftArticle;
import me.kimyeonsup.blog.menu.domain.entity.Menu;

@Getter
public class AddDraftArticle {

    private final Long id;
    private final String title;
    private final String subTitle;
    private final String content;
    private final Long menuId;

    public AddDraftArticle(Long id, String title, String subTitle, String content, Long menuId) {
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
