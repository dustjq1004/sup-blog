package me.kimyeonsup.home.domain.blog.draft.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.kimyeonsup.home.domain.blog.admin.menu.domain.entity.Menu;
import me.kimyeonsup.home.domain.blog.draft.domain.entity.DraftArticle;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDraftArticle {

    private Long id;
    private String title;
    private String subTitle;
    private String content;
    private Long menuId;

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
