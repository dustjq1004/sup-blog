package me.kimyeonsup.home.domain.blog.admin.menu.controller;

import lombok.RequiredArgsConstructor;
import me.kimyeonsup.home.domain.blog.admin.menu.domain.dto.AddCategoryRequest;
import me.kimyeonsup.home.domain.blog.admin.menu.domain.dto.CategoriesResponse;
import me.kimyeonsup.home.domain.blog.admin.menu.domain.dto.CategoryResponse;
import me.kimyeonsup.home.domain.blog.admin.menu.domain.dto.DeleteCategoryRequest;
import me.kimyeonsup.home.domain.blog.admin.menu.domain.dto.UpdateCategoryRequest;
import me.kimyeonsup.home.domain.blog.admin.menu.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CategoryApiController {

    private final CategoryService categoryService;

    @GetMapping("/categories")
    public ResponseEntity<CategoriesResponse> findCategories() {
        CategoriesResponse response = CategoriesResponse.ofNotContainsMenu(categoryService.findAll());
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/category")
    public ResponseEntity<CategoryResponse> findCategories(@RequestParam long categoryId) {
        CategoryResponse category = categoryService.findById(categoryId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(category);
    }

    @PostMapping("/category")
    public ResponseEntity<CategoryResponse> addCategory(@Validated @RequestBody AddCategoryRequest request) {
        CategoryResponse savedCategory = categoryService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedCategory);
    }

    @DeleteMapping("/category/delete")
    public ResponseEntity<Void> deleteCategory(@RequestBody DeleteCategoryRequest request) {
        categoryService.delete(request.getCategoryId());

        return ResponseEntity.ok()
                .build();
    }

    @PutMapping("/category/update")
    public ResponseEntity<CategoryResponse> updateCategory(@RequestBody UpdateCategoryRequest request) {
        CategoryResponse updatedCategory = categoryService.update(request);
        return ResponseEntity.ok()
                .body(updatedCategory);
    }
}
