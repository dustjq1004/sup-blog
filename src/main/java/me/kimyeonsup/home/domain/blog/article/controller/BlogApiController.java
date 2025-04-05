package me.kimyeonsup.home.domain.blog.article.controller;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import me.kimyeonsup.home.config.oauth.PrincipalDetail;
import me.kimyeonsup.home.domain.blog.article.domain.dto.AddArticleRequest;
import me.kimyeonsup.home.domain.blog.article.domain.dto.ArticleListViewResponse;
import me.kimyeonsup.home.domain.blog.article.domain.dto.ArticleResponse;
import me.kimyeonsup.home.domain.blog.article.domain.dto.PaginationResponse;
import me.kimyeonsup.home.domain.blog.article.domain.dto.UpdateArticleRequest;
import me.kimyeonsup.home.domain.blog.article.domain.entity.Article;
import me.kimyeonsup.home.domain.blog.article.service.ArticleService;
import me.kimyeonsup.home.domain.blog.draft.service.DraftService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class BlogApiController {

    private final ArticleService articleService;
    private final DraftService draftService;

    @GetMapping("/api/articles")
    public ResponseEntity<PaginationResponse<ArticleListViewResponse>> findArticles(
            @RequestParam(required = false, defaultValue = "0") int pageNumber,
            @RequestParam(required = false) String menuName) {

        Page<Article> articles = articleService.findAllPagenation(pageNumber, menuName);

        PaginationResponse<ArticleListViewResponse> paginationResponse = new PaginationResponse<>(
                articles.getNumber(), articles.getSize(), articles.getNumberOfElements(), articles.isFirst(),
                articles.isLast(), articles.getTotalPages(), articles.getTotalElements(),
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
    public ResponseEntity<ArticleResponse> addArticle(@Validated @RequestBody AddArticleRequest request,
                                                      @AuthenticationPrincipal PrincipalDetail user) {
        Article savedArticle = articleService.save(request, user.getName());
        Long draftId = request.getDraftId();
        if (!Objects.isNull(draftId)) {
            draftService.delete(draftId);
        }
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
