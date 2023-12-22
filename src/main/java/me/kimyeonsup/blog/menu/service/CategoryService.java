package me.kimyeonsup.blog.menu.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import me.kimyeonsup.blog.menu.domain.dto.AddCategoryRequest;
import me.kimyeonsup.blog.menu.domain.dto.UpdateCategoryRequest;
import me.kimyeonsup.blog.menu.domain.entity.Category;
import me.kimyeonsup.blog.menu.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category save(AddCategoryRequest request) {
        return categoryRepository.save(request.toEntity());
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
    public Category update(long id, UpdateCategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));
        category.update(request.getName());
        return category;
    }
}
