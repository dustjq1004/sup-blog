package me.kimyeonsup.home.domain.blog.menu.controller;

import lombok.RequiredArgsConstructor;
import me.kimyeonsup.home.domain.blog.menu.domain.dto.AddCategoryRequest;
import me.kimyeonsup.home.domain.blog.menu.domain.dto.CategoryResponse;
import me.kimyeonsup.home.domain.blog.menu.domain.dto.UpdateCategoryRequest;
import me.kimyeonsup.home.domain.blog.menu.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CategoryApiController {

    private final CategoryService categoryService;

    @PostMapping("/category")
    public ResponseEntity<CategoryResponse> addCategory(@Validated @RequestBody AddCategoryRequest request) {
        CategoryResponse savedCategory = categoryService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedCategory);
    }

    @DeleteMapping("/category/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable long id) {
        categoryService.delete(id);
        return ResponseEntity.ok()
                .build();
    }

    @PutMapping("/category/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable long id,
                                                           @RequestBody UpdateCategoryRequest request) {
        CategoryResponse updatedCategory = categoryService.update(id, request);

        return ResponseEntity.ok()
                .body(updatedCategory);
    }
}
