package me.kimyeonsup.home.domain.blog.draft.domain.dto;

import lombok.Getter;
import me.kimyeonsup.home.domain.blog.draft.domain.entity.DraftArticle;

@Getter
public class DraftArticlesResponse {

    private final Long id;
    private final String title;
    private final String subTitle;
    private final String content;
    private final String author;
    private final Long menuId;

    public DraftArticlesResponse(DraftArticle draftArticle) {
        this.id = draftArticle.getId();
        this.title = draftArticle.getTitle();
        this.subTitle = draftArticle.getSubTitle();
        this.content = draftArticle.getContent();
        this.author = draftArticle.getAuthor();
        this.menuId = draftArticle.getMenu().getId();
    }
}
