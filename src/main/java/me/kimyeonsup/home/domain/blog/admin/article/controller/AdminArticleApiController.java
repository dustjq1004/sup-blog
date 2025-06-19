package me.kimyeonsup.home.domain.blog.admin.article.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import me.kimyeonsup.home.domain.blog.admin.article.domain.dto.AdminArticlesPaginationResponse;
import me.kimyeonsup.home.domain.blog.admin.article.domain.dto.AdminArticlesResponse;
import me.kimyeonsup.home.domain.blog.admin.article.domain.dto.ArticleBatchDeleteRequest;
import me.kimyeonsup.home.domain.blog.admin.article.domain.dto.ArticleBatchDeleteResponse;
import me.kimyeonsup.home.domain.blog.admin.article.domain.dto.ArticleSelectCondition;
import me.kimyeonsup.home.domain.blog.admin.article.domain.entity.AdminArticle;
import me.kimyeonsup.home.domain.blog.admin.article.service.AdminArticleService;
import me.kimyeonsup.home.domain.blog.admin.pagination.PageRequestBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminArticleApiController {

    private final AdminArticleService adminArticleService;

    @GetMapping("/api/admin/articles")
    public ResponseEntity<AdminArticlesPaginationResponse<AdminArticlesResponse>> findArticles(
            @RequestParam(required = false) int pageNumber,
            @RequestParam(required = false, defaultValue = "10") int pageSize,
            @RequestParam(required = false, defaultValue = "createdAt") String sortBy,
            @RequestParam(required = false, defaultValue = "DESC") String direction,
            @RequestParam(required = false, defaultValue = "") String title,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long menuId) {

        PageRequest articlePageRequest = PageRequestBuilder.builder()
                .pageSize(pageSize)
                .pageNumber(pageNumber)
                .sortBy(sortBy)
                .direction(direction)
                .build()
                .createPageRequest();

        ArticleSelectCondition params = ArticleSelectCondition.builder()
                .categoryId(categoryId)
                .menuId(menuId)
                .title(title)
                .build();

        Page<AdminArticle> articles = adminArticleService.findArticlesBy(articlePageRequest, params);

        AdminArticlesPaginationResponse<AdminArticlesResponse> articlesDto = new AdminArticlesPaginationResponse<>(
                articles.getNumber(),
                articles.getSize(),
                articles.getNumberOfElements(),
                articles.isFirst(),
                articles.isLast(),
                articles.getTotalPages(),
                articles.getTotalElements(),
                articles.stream().map(AdminArticlesResponse::new).toList());

        return ResponseEntity.ok()
                .body(articlesDto);
    }

    /**
     * 게시글 일괄 삭제
     *
     * @param params 삭제할 게시글 ID 목록
     * @return ResponseEntity<ArticleBatchDeleteResponse>
     */
    @DeleteMapping("/api/admin/articles/batch")
    public ResponseEntity<ArticleBatchDeleteResponse> deleteArticlesByIds(
            @RequestBody ArticleBatchDeleteRequest params) {
        int deletedCount = adminArticleService.deleteArticlesByIds(List.of(params.getArticleIds()));
        return ResponseEntity.ok()
                .body(ArticleBatchDeleteResponse.builder()
                        .deletedCount(deletedCount)
                        .build());
    }
}
