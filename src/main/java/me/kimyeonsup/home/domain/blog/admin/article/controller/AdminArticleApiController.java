package me.kimyeonsup.home.domain.blog.admin.article.controller;

import lombok.RequiredArgsConstructor;
import me.kimyeonsup.home.domain.blog.admin.article.domain.dto.ArticleSelectCondition;
import me.kimyeonsup.home.domain.blog.admin.article.domain.entity.AdminArticle;
import me.kimyeonsup.home.domain.blog.admin.article.service.AdminArticleService;
import me.kimyeonsup.home.domain.blog.admin.pagination.PageRequestBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AdminArticleApiController {

    private final AdminArticleService adminArticleService;

    @GetMapping("/api/admin/articles")
    public ResponseEntity<Page<AdminArticle>> findArticles(
            @RequestParam(required = false) int pageNumber,
            @RequestParam(required = false) int pageSize,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long menuId) {

        PageRequest articlePageRequest = PageRequestBuilder.builder()
                .pageSize(pageSize)
                .pageNumber(pageNumber)
                .sortBy(sortBy)
                .direction(direction)
                .build().createPageRequest();
        ArticleSelectCondition params = ArticleSelectCondition.builder()
                .categoryId(categoryId)
                .menuId(menuId)
                .build();

        Page<AdminArticle> articles = adminArticleService.findArticlesBy(articlePageRequest, params);

        return ResponseEntity.ok()
                .body(articles);
    }
}
