package me.kimyeonsup.blog.menu.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import me.kimyeonsup.blog.menu.domain.entity.Category;

@NoArgsConstructor
@RequiredArgsConstructor
@Getter
public class AddCategoryRequest {

    private String name;

    public Category toEntity() {
        return Category.builder()
                .name(name)
                .build();
    }
}
