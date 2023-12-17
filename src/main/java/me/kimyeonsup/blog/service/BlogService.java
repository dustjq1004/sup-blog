package me.kimyeonsup.blog.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import me.kimyeonsup.blog.article.domain.dto.AddArticleRequest;
import me.kimyeonsup.blog.article.domain.dto.UpdateArticleRequest;
import me.kimyeonsup.blog.article.domain.entity.Article;
import me.kimyeonsup.blog.article.repository.ArticleRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BlogService {

    private final ArticleRepository articleRepository;

    public Article save(AddArticleRequest request, String userName) {
        return articleRepository.save(request.toEntity(userName));
    }

    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    public Article findById(long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));
    }

    public void delete(long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));

        authorizeArticleAuthor(article);
        articleRepository.delete(article);
    }


    @Transactional
    public Article update(long id, UpdateArticleRequest request) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));

        authorizeArticleAuthor(article);
        article.update(request.getTitle(), request.getContent());

        return article;
    }

    private void authorizeArticleAuthor(Article article) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!article.getAuthor().equals(userName)) {
            throw new IllegalArgumentException("not authorized");
        }
    }
}
