package me.kimyeonsup.home.domain.blog.article.domain.dto;

import lombok.Getter;
import me.kimyeonsup.home.domain.blog.article.domain.entity.Article;
import me.kimyeonsup.home.util.DateTimeFormat;
import me.kimyeonsup.home.util.StringUtils;

@Getter
public class ArticleListViewResponse {

    private final Long id;
    private final String title;
    private final String subTitle;
    private final String content;
    private final String thumbnailUrl;
    private final String menuName;
    private final String updatedAt;

    public ArticleListViewResponse(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.subTitle = article.getSubTitle();
        this.content = StringUtils.replaceAllSpecialCharacter(article.getContent());
        this.thumbnailUrl = article.getThumbnailUrl();
        this.menuName = article.getMenu().getName();
        this.updatedAt = DateTimeFormat.diffDateFromNow(article.getUpdatedAt());
    }
}
