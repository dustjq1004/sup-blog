package me.kimyeonsup.blog.menu.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor
@RequiredArgsConstructor
@Getter
public class UpdateCategoryRequest {
    private String name;
}
