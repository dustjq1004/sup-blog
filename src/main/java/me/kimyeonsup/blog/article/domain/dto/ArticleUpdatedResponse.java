package me.kimyeonsup.blog.article.domain.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.kimyeonsup.blog.article.domain.entity.Article;

@NoArgsConstructor
@Getter
public class ArticleUpdatedResponse {

    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private String author;

    public ArticleUpdatedResponse(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.createdAt = article.getCreatedAt();
        this.author = article.getAuthor();
    }
}
