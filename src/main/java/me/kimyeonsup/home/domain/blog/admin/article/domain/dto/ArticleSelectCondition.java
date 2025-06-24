package me.kimyeonsup.home.domain.blog.admin.article.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class ArticleSelectCondition {
    private Long categoryId;
    private Long menuId;
    private String title;

    @Builder
    public ArticleSelectCondition(Long categoryId, Long menuId, String title) {
        this.categoryId = categoryId;
        this.menuId = menuId;
        this.title = title;
    }
}
