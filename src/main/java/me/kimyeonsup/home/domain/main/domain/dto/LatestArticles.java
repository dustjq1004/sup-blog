package me.kimyeonsup.home.domain.main.domain.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import me.kimyeonsup.home.domain.blog.article.domain.dto.ArticleResponse;

import java.util.List;

@Getter
@NoArgsConstructor
public class LatestArticles {

    List<ArticleResponse> articles;

    public LatestArticles(List<ArticleResponse> articles) {
        this.articles = articles;
    }
}
