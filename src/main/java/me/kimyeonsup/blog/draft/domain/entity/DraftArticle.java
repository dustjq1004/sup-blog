package me.kimyeonsup.blog.draft.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.kimyeonsup.blog.draft.domain.dto.UpdateDraftArticle;
import me.kimyeonsup.blog.global.common.entity.BaseTimeEntity;
import me.kimyeonsup.blog.menu.domain.entity.Menu;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DraftArticle extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "sub_title")
    private String subTitle;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "delete_yn", nullable = false)
    Boolean deleteYn = false;

    @Column(name = "author", nullable = false)
    String author;

    @ManyToOne
    private Menu menu;

    @Builder
    public DraftArticle(String title, String subTitle, String content, Menu menu, String author) {
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;
        this.menu = menu;
        this.author = author;
    }

    public DraftArticle updateAll(UpdateDraftArticle draftArticle) {
        this.title = draftArticle.getTitle();
        this.subTitle = draftArticle.getSubTitle();
        this.content = draftArticle.getContent();
        this.menu = Menu.builder().id(menu.getId()).build();
        return this;
    }

    public void delete() {
        this.deleteYn = true;
    }
}
