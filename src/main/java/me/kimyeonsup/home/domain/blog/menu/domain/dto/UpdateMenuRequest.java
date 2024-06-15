package me.kimyeonsup.home.domain.blog.menu.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateMenuRequest {
    private Long categoryId;
    private String name;
}
