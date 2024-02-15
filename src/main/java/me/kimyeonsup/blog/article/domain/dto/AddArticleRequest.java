package me.kimyeonsup.blog.article.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.kimyeonsup.blog.article.domain.entity.Article;
import me.kimyeonsup.blog.menu.domain.entity.Menu;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddArticleRequest {

    private String title;
    private String content;
    private Long menuId;

    public Article toEntity(String author) {
        return Article.builder()
                .title(title)
                .content(content)
                .menu(Menu.builder().id(menuId).build())
                .author(author)
                .build();
    }
}
