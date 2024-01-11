package me.kimyeonsup.blog.article.domain.dto;

import lombok.Getter;
import me.kimyeonsup.blog.article.domain.entity.Article;
import me.kimyeonsup.blog.util.StringUtils;

@Getter
public class ArticleListViewResponse {

    private final Long id;
    private final String title;
    private final String content;

    public ArticleListViewResponse(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = StringUtils.replaceAllSpecialCharacter(article.getContent());
    }
}
