package me.kimyeonsup.home.domain.blog.article.domain.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import me.kimyeonsup.home.domain.blog.article.domain.entity.Article;
import me.kimyeonsup.home.util.CommonMarkUtil;

@Getter
public class ArticleResponse {

    private final Long id;
    private final String title;
    private final String subTitle;
    private final String content;
    private final String author;
    private final String menuName;
    private final LocalDateTime updateAt;

    public ArticleResponse(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.subTitle = article.getSubTitle();
        this.content = CommonMarkUtil.renderHtml(article.getContent());
        this.author = article.getAuthor();
        this.menuName = article.getMenu().getName();
        this.updateAt = article.getUpdatedAt();
    }
}
