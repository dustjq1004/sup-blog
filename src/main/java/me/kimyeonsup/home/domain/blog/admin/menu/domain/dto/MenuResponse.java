package me.kimyeonsup.home.domain.blog.admin.menu.domain.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.kimyeonsup.home.domain.blog.admin.menu.domain.entity.Menu;
import me.kimyeonsup.home.util.DateTimeFormat;

@NoArgsConstructor
@Getter
public class MenuResponse {

    private Long id;
    private Long categoryId;
    private String name;
    private String description;
    private String createdAt;
    private String updatedAt;

    public MenuResponse(Menu menu) {
        this.id = menu.getId();
        this.categoryId = menu.getCategory().getId();
        this.name = menu.getName();
    }

    @Builder
    public MenuResponse(Long id, Long categoryId, String name, String description, LocalDateTime createdAt,
                        LocalDateTime updatedAt) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.createdAt = DateTimeFormat.diffDateFromNow(createdAt);
        this.updatedAt = DateTimeFormat.diffDateFromNow(updatedAt);
    }
}
