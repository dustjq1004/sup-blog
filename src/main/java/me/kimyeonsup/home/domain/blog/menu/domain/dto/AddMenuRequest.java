package me.kimyeonsup.home.domain.blog.menu.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.kimyeonsup.home.domain.blog.menu.domain.entity.Category;
import me.kimyeonsup.home.domain.blog.menu.domain.entity.Menu;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddMenuRequest {

    @NotNull
    private Long categoryId;

    @NotBlank
    private String name;

    public Menu toEntity(Category category) {
        return Menu.builder()
                .name(name)
                .category(category)
                .build();
    }
}
