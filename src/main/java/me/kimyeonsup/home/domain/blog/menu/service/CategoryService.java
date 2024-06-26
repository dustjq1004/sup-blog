package me.kimyeonsup.home.domain.blog.menu.service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import me.kimyeonsup.home.domain.blog.menu.domain.dto.AddCategoryRequest;
import me.kimyeonsup.home.domain.blog.menu.domain.dto.CategoryResponse;
import me.kimyeonsup.home.domain.blog.menu.domain.dto.UpdateCategoryRequest;
import me.kimyeonsup.home.domain.blog.menu.domain.entity.Category;
import me.kimyeonsup.home.domain.blog.menu.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryResponse save(AddCategoryRequest request) {
        return new CategoryResponse(categoryRepository.save(request.toEntity()));
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public void delete(long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));
        categoryRepository.delete(category);
    }

    @Transactional
    public CategoryResponse update(long id, UpdateCategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));
        category.update(request.getName());
        return new CategoryResponse(category);
    }
}
