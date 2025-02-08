package me.kimyeonsup.home.domain.blog.admin.menu.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import me.kimyeonsup.home.domain.blog.admin.menu.domain.dto.AddCategoryRequest;
import me.kimyeonsup.home.domain.blog.admin.menu.domain.dto.CategoryResponse;
import me.kimyeonsup.home.domain.blog.admin.menu.domain.dto.UpdateCategoryRequest;
import me.kimyeonsup.home.domain.blog.admin.menu.domain.entity.Category;
import me.kimyeonsup.home.domain.blog.admin.menu.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryResponse save(AddCategoryRequest request) {
        return new CategoryResponse(categoryRepository.save(request.toEntity()));
    }

    public CategoryResponse findById(long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not find By id : " + id));
        return CategoryResponse.builder()
                .id(category.getId())
                .emoji(category.getEmoji())
                .name(category.getName())
                .build();
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Transactional
    public void delete(long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));

        if (category.hasMenus()) {
            throw new IllegalArgumentException("has menus : " + id);
        }

        categoryRepository.delete(category);
    }

    @Transactional
    public CategoryResponse update(UpdateCategoryRequest request) {
        long id = request.getId();
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));
        category.update(request.getName());
        return CategoryResponse.builder()
                .id(id)
                .emoji(category.getEmoji())
                .name(category.getName())
                .updatedAt(category.getUpdatedAt())
                .build();
    }
}
