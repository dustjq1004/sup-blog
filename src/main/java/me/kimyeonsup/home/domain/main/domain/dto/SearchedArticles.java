package me.kimyeonsup.home.domain.main.domain.dto;

import me.kimyeonsup.home.domain.blog.article.domain.dto.ArticleListViewResponse;

import java.util.List;

public class SearchedArticles {
    List<ArticleListViewResponse> articles;

    public SearchedArticles(List<ArticleListViewResponse> articles) {
        this.articles = articles;
    }
}
