package me.kimyeonsup.blog.menu.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.kimyeonsup.blog.menu.domain.entity.Menu;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddMenuRequest {

    private Long categoryId;
    private String name;

    public Menu toEntity() {
        return Menu.builder()
                .name(name)
                .categoryId(categoryId)
                .build();
    }
}
