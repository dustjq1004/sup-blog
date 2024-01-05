package me.kimyeonsup.blog.menu.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.kimyeonsup.blog.menu.domain.entity.Category;
import me.kimyeonsup.blog.menu.domain.entity.Menu;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddMenuRequest {

    private Long categoryId;
    private String name;

    public Menu toEntity(Category category) {
        return Menu.builder()
                .name(name)
                .category(category)
                .build();
    }
}
