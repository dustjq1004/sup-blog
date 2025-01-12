package me.kimyeonsup.home.domain.blog.admin.menu.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import me.kimyeonsup.home.domain.blog.admin.menu.domain.dto.AddMenuRequest;
import me.kimyeonsup.home.domain.blog.admin.menu.domain.dto.MenuResponse;
import me.kimyeonsup.home.domain.blog.admin.menu.domain.dto.MenusResponse;
import me.kimyeonsup.home.domain.blog.admin.menu.domain.dto.UpdateMenuRequest;
import me.kimyeonsup.home.domain.blog.admin.menu.domain.entity.Category;
import me.kimyeonsup.home.domain.blog.admin.menu.domain.entity.Menu;
import me.kimyeonsup.home.domain.blog.admin.menu.repository.CategoryRepository;
import me.kimyeonsup.home.domain.blog.admin.menu.repository.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final CategoryRepository categoryRepository;

    public MenuResponse save(AddMenuRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("not found: " + request.getCategoryId()));
        return new MenuResponse(menuRepository.save(request.toEntity(category)));
    }

    public List<Menu> findAll() {
        return menuRepository.findAll();
    }

    public MenusResponse findByCategory(Long categoryId) {
        List<Menu> menus = menuRepository.findByCategoryId(categoryId);
        return MenusResponse.builder()
                .menus(menus.stream().map(menu -> MenuResponse.builder()
                                .id(menu.getId())
                                .name(menu.getName())
                                .description(menu.getDescription())
                                .createdAt(menu.getCreatedAt())
                                .updatedAt(menu.getUpdatedAt())
                                .build())
                        .toList())
                .build();
    }

    public void delete(long id) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));
        menuRepository.delete(menu);
    }

    @Transactional
    public MenuResponse update(long id, UpdateMenuRequest request) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("not found : " + request.getCategoryId()));
        menu.update(request.getName(), category);
        return new MenuResponse(menu);
    }
}
