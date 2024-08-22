package me.kimyeonsup.home.domain.main.domain.dto;

import me.kimyeonsup.home.domain.blog.article.domain.dto.ArticleResponse;

import java.util.List;

public class SearchedArticles {
    List<ArticleResponse> articles;

    public SearchedArticles(List<ArticleResponse> articles) {
        this.articles = articles;
    }
}
