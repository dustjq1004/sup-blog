package me.kimyeonsup.blog.menu.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.kimyeonsup.blog.menu.domain.entity.Menu;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MenuResponse {

    private Long id;
    private Long categoryId;
    private String name;

    public MenuResponse(Menu menu) {
        this.id = menu.getId();
        this.categoryId = menu.getCategory().getId();
        this.name = menu.getName();
    }
}
