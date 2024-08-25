package me.kimyeonsup.home.domain.main.service;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import me.kimyeonsup.home.domain.blog.article.domain.dto.ArticleListViewResponse;
import me.kimyeonsup.home.domain.blog.article.repository.ArticleRepository;
import me.kimyeonsup.home.domain.main.domain.dto.LatestArticles;
import me.kimyeonsup.home.domain.main.domain.dto.SearchedArticles;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MainService {

    private final ArticleRepository articleRepository;

    public LatestArticles getLatestArticles() {

        return new LatestArticles(articleRepository
                .findTop6ByOrderByCreatedAtDesc()
                .stream()
                .map(ArticleListViewResponse::new)
                .toList());
    }

    public SearchedArticles getArticlesByTitle(@NotBlank String titleParam) {
        return new SearchedArticles(articleRepository.findByTitle(titleParam)
                .stream()
                .map(ArticleListViewResponse::new)
                .toList());
    }
}
