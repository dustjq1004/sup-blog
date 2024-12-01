package me.kimyeonsup.home.domain.blog.article.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.kimyeonsup.home.domain.blog.article.domain.entity.Article;
import me.kimyeonsup.home.domain.blog.article.domain.vo.Thumbnail;
import me.kimyeonsup.home.domain.blog.admin.menu.domain.entity.Menu;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddArticleRequest {

    @NotBlank
    @Size(max = 100)
    private String title;

    @Size(max = 100)
    private String subTitle;
    private String content;

    @NotNull
    private Long menuId;

    private Long draftId;

    public Article toEntity(String author) {
        return Article.builder()
                .title(title)
                .content(content)
                .subTitle(subTitle)
                .menu(Menu.builder().id(menuId).build())
                .thumbnailUrl(Thumbnail.of(content))
                .author(author)
                .build();
    }
}
