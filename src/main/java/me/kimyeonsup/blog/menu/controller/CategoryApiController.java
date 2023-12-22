package me.kimyeonsup.blog.menu.controller;

import lombok.RequiredArgsConstructor;
import me.kimyeonsup.blog.menu.domain.dto.AddCategoryRequest;
import me.kimyeonsup.blog.menu.domain.dto.CategoryResponse;
import me.kimyeonsup.blog.menu.domain.entity.Category;
import me.kimyeonsup.blog.menu.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CategoryApiController {

    private final CategoryService categoryService;

    @PostMapping("/category")
    public ResponseEntity<CategoryResponse> addCategory(@RequestBody AddCategoryRequest request) {
        Category savedCategory = categoryService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CategoryResponse(savedCategory));
    }
}
