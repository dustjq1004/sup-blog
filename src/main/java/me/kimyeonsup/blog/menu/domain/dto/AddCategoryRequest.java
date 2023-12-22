package me.kimyeonsup.blog.menu.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.kimyeonsup.blog.menu.domain.entity.Category;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddCategoryRequest {

    private String name;

    public Category toEntity() {
        return Category.builder()
                .name(name)
                .build();
    }
}
