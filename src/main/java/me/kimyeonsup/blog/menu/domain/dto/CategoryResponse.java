package me.kimyeonsup.blog.menu.domain.dto;

import java.util.List;
import lombok.Getter;
import me.kimyeonsup.blog.menu.domain.entity.Category;
import me.kimyeonsup.blog.menu.domain.entity.Menu;

@Getter
public class CategoryResponse {

    private Long id;
    private String name;
    private List<Menu> menus;

    public CategoryResponse(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.menus = category.getMenus();
    }
}
