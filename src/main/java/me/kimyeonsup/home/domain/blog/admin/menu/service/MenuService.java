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

    public MenusResponse findAll() {
        List<MenuResponse> menus = menuRepository.findAll().stream().map(menu ->
                MenuResponse.builder()
                        .id(menu.getId())
                        .emoji(menu.getEmoji())
                        .name(menu.getName())
                        .description(menu.getDescription())
                        .updatedAt(menu.getUpdatedAt())
                        .build()
        ).toList();
        return new MenusResponse(menus);
    }

    public MenusResponse findByCategory(Long categoryId) {
        List<Menu> menus = menuRepository.findByCategoryId(categoryId);
        return MenusResponse.builder()
                .menus(menus.stream().map(menu -> MenuResponse.builder()
                                .id(menu.getId())
                                .emoji(menu.getEmoji())
                                .name(menu.getName())
                                .description(menu.getDescription())
                                .categoryId(menu.getCategory().getId())
                                .createdAt(menu.getCreatedAt())
                                .updatedAt(menu.getUpdatedAt())
                                .build())
                        .toList())
                .build();
    }

    @Transactional
    public MenuResponse delete(long id) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));

        if (menu.hasArticles()) {
            throw new IllegalStateException("has articles, id : " + id);
        }

        menuRepository.delete(menu);
        return new MenuResponse(menu);
    }

    @Transactional
    public MenuResponse update(UpdateMenuRequest request) {
        long id = request.getId();
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("not found : " + request.getCategoryId()));

        menu.update(request, category);
        return new MenuResponse(menu);
    }

    public MenuResponse findById(long menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + menuId));
        return MenuResponse.builder()
                .id(menu.getId())
                .emoji(menu.getEmoji())
                .name(menu.getName())
                .description(menu.getDescription())
                .categoryId(menu.getCategory().getId())
                .categoryEmoji(menu.getCategory().getEmoji())
                .categoryName(menu.getCategory().getName())
                .createdAt(menu.getCreatedAt())
                .updatedAt(menu.getUpdatedAt())
                .build();
    }

    public MenuResponse findByName(String menuName) {
        Menu menu = menuRepository.findByName(menuName)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + menuName));
        return MenuResponse.builder()
                .id(menu.getId())
                .emoji(menu.getEmoji())
                .name(menu.getName())
                .description(menu.getDescription())
                .createdAt(menu.getCreatedAt())
                .updatedAt(menu.getUpdatedAt())
                .build();
    }
}
