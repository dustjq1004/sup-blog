package me.kimyeonsup.home.domain.main.service;

import lombok.RequiredArgsConstructor;
import me.kimyeonsup.home.domain.blog.article.domain.dto.ArticleResponse;
import me.kimyeonsup.home.domain.blog.article.repository.ArticleRepository;
import me.kimyeonsup.home.domain.main.domain.dto.LatestArticles;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MainService {

    private final ArticleRepository articleRepository;

    public LatestArticles getLatestArticles() {

        return new LatestArticles(articleRepository
                .findTop6ByOrderByCreatedAtDesc()
                .stream()
                .map(ArticleResponse::new)
                .toList());
    }
}
