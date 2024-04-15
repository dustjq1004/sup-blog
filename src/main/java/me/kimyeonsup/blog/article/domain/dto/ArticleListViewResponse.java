package me.kimyeonsup.blog.article.domain.dto;

import lombok.Getter;
import me.kimyeonsup.blog.article.domain.entity.Article;
import me.kimyeonsup.blog.util.StringUtils;

import java.time.LocalDateTime;

@Getter
public class ArticleListViewResponse {

    private final Long id;
    private final String title;
    private final String subTitle;
    private final String content;
    private final String thumbnailUrl;
    private final String menuName;
    private final LocalDateTime updatedAt;

    public ArticleListViewResponse(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.subTitle = article.getSubTitle();
        this.content = StringUtils.replaceAllSpecialCharacter(article.getContent());
        this.thumbnailUrl = article.getThumbnailUrl();
        this.menuName = article.getMenu().getName();
        this.updatedAt = article.getUpdatedAt();
    }
}
