package me.kimyeonsup.blog.article.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import me.kimyeonsup.blog.article.domain.dto.AddArticleRequest;
import me.kimyeonsup.blog.article.domain.dto.ArticlePrevNextDto;
import me.kimyeonsup.blog.article.domain.dto.ArticlePrevNextResponse;
import me.kimyeonsup.blog.article.domain.dto.UpdateArticleRequest;
import me.kimyeonsup.blog.article.domain.entity.Article;
import me.kimyeonsup.blog.article.repository.ArticleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ArticleService {

    private static final int PAGE_SIZE = 12;
    private static final String ORDER_CRITERIA = "createdAt";

    private final ArticleRepository articleRepository;

    public Article save(AddArticleRequest request, String userName) {
        Article savedArticle = articleRepository.save(request.toEntity(userName));
        return savedArticle;
    }

    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    public Page<Article> findAllPagenation(int pageNumber, String menuName) {
        Pageable pageRequest = PageRequest.of(pageNumber, PAGE_SIZE, Sort.by(Direction.ASC, ORDER_CRITERIA));

        Page<Article> articles = Optional.ofNullable(menuName)
                .map(s -> articleRepository.findByMenuName(pageRequest, menuName))
                .orElseGet(() -> articleRepository.findAll(pageRequest));

        return articles;
    }

    public List<Article> findByMenuId(Long menuId) {
        return articleRepository.findByMenuId(menuId);
    }

    public List<Article> findByMenuName(String menuName) {
        return articleRepository.findByMenuName(menuName);
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

    public ArticlePrevNextResponse findPrevNextArticle(long id, String menuName) {
        ArticlePrevNextDto prevNextArticle = articleRepository.findPrevNextArticle(id, menuName);
        return ArticlePrevNextResponse.builder()
                .articlePrevNextDto(prevNextArticle)
                .build();
    }

    private void authorizeArticleAuthor(Article article) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!article.getAuthor().equals(userName)) {
            throw new IllegalArgumentException("not authorized");
        }
    }
}
