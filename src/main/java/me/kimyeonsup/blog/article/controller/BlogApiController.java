package me.kimyeonsup.blog.article.controller;

import lombok.RequiredArgsConstructor;
import me.kimyeonsup.blog.article.domain.dto.*;
import me.kimyeonsup.blog.article.domain.entity.Article;
import me.kimyeonsup.blog.article.service.ArticleService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
public class BlogApiController {

    private final ArticleService articleService;

    @GetMapping("/api/articles")
    public ResponseEntity<PaginationResponse<ArticleListViewResponse>> findArticles(
            @RequestParam(required = false, defaultValue = "0") int pageNumber,
            @RequestParam(required = false) String menuName) {

        Page<Article> articles = articleService.findAllPagenation(pageNumber, menuName);

        PaginationResponse<ArticleListViewResponse> paginationResponse = new PaginationResponse<>(
                pageNumber, articles.isLast(), articles.getTotalElements(),
                articles.stream()
                        .map(ArticleListViewResponse::new)
                        .toList());

        return ResponseEntity.ok()
                .body(paginationResponse);
    }

    @GetMapping("/api/articles/{id}")
    public ResponseEntity<ArticleResponse> findArticle(@PathVariable long id) {
        Article article = articleService.findById(id);

        return ResponseEntity.ok()
                .body(new ArticleResponse(article));
    }

    @PostMapping("/api/articles")
    public ResponseEntity<ArticleResponse> addArticle(@Validated @RequestBody AddArticleRequest request, Principal principal) {
        Article savedArticle = articleService.save(request, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ArticleResponse(savedArticle));
    }

    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable long id) {
        articleService.delete(id);

        return ResponseEntity.ok()
                .build();
    }

    @PutMapping("/api/articles/{id}")
    public ResponseEntity<ArticleResponse> updateArticle(@PathVariable long id,
                                                         @Validated @RequestBody UpdateArticleRequest request) {
        Article updatedArticle = articleService.update(id, request);

        return ResponseEntity.ok()
                .body(new ArticleResponse(updatedArticle));
    }
}
