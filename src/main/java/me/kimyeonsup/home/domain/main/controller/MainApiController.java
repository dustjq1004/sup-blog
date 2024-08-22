package me.kimyeonsup.home.domain.main.controller;

import lombok.RequiredArgsConstructor;
import me.kimyeonsup.home.domain.main.domain.dto.LatestArticles;
import me.kimyeonsup.home.domain.main.domain.dto.SearchParams;
import me.kimyeonsup.home.domain.main.domain.dto.SearchedArticles;
import me.kimyeonsup.home.domain.main.service.MainService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/main")
public class MainApiController {

    private final MainService mainService;

    @GetMapping("/articles/latest")
    public ResponseEntity<LatestArticles> getLatestArticles() {
        return ResponseEntity.ok()
                .body(mainService.getLatestArticles());
    }

    @GetMapping("/articles/search")
    public ResponseEntity<SearchedArticles> getSearchedArticles(@RequestParam SearchParams searchParams) {
        return ResponseEntity.ok()
                .body(mainService.getArticlesByTitle(searchParams.getTitleParam()));
    }
}
