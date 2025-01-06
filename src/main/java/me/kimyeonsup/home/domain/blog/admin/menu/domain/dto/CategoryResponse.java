package me.kimyeonsup.home.domain.blog.admin.menu.domain.dto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.Builder;
import lombok.Getter;
import me.kimyeonsup.home.domain.blog.admin.menu.domain.entity.Category;

@Getter
public class CategoryResponse {

    private Long id;
    private String name;
    private String emoji;
    private List<MenuResponse> menus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CategoryResponse(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.emoji = category.getEmoji();
        this.menus = Optional.ofNullable(category.getMenus())
                .orElseGet(Collections::emptyList)
                .stream()
                .map(MenuResponse::new)
                .toList();
    }

    @Builder
    public CategoryResponse(Long id, String name, String emoji, List<MenuResponse> menus, LocalDateTime createdAt,
                            LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.menus = menus;
        this.emoji = emoji;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
