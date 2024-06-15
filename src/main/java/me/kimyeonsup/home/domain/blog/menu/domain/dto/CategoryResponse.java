package me.kimyeonsup.home.domain.blog.menu.domain.dto;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import lombok.Getter;
import me.kimyeonsup.home.domain.blog.menu.domain.entity.Category;

@Getter
public class CategoryResponse {

    private Long id;
    private String name;
    private List<MenuResponse> menus;

    public CategoryResponse(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.menus = Optional.ofNullable(category.getMenus())
                .orElseGet(Collections::emptyList)
                .stream()
                .map(MenuResponse::new)
                .toList();
    }
}
