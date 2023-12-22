package me.kimyeonsup.blog.menu.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import me.kimyeonsup.blog.menu.domain.dto.CategoryResponse;
import me.kimyeonsup.blog.menu.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class NavigatorApiController {

    private final CategoryService categoryService;

    @GetMapping("/navigate")
    public ResponseEntity<List<CategoryResponse>> findAllCategories() {
        List<CategoryResponse> categories = categoryService.findAll()
                .stream()
                .map(CategoryResponse::new)
                .toList();
        return ResponseEntity.ok()
                .body(categories);
    }
}
