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
    private List<MenuResponse> menus;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public CategoryResponse(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.menus = Optional.ofNullable(category.getMenus())
                .orElseGet(Collections::emptyList)
                .stream()
                .map(MenuResponse::new)
                .toList();
    }

    @Builder
    public CategoryResponse(Long id, String name, List<MenuResponse> menus, LocalDateTime createAt,
                            LocalDateTime updateAt) {
        this.id = id;
        this.name = name;
        this.menus = menus;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }
}
