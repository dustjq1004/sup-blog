package me.kimyeonsup.home.domain.blog.admin.article.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminArticleApiController {

    @GetMapping("/api/admin/articles")
    public ResponseEntity<String> findArticles(
            @RequestParam(required = false, defaultValue = "0") int pageNumber,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long menuId) {

        return ResponseEntity.ok()
                .body("ok");
    }
}
