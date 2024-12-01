package me.kimyeonsup.home.domain.blog.article.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.kimyeonsup.home.domain.blog.article.domain.vo.Thumbnail;
import me.kimyeonsup.home.domain.blog.admin.menu.domain.entity.Menu;
import me.kimyeonsup.home.global.common.entity.BaseTimeEntity;

import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article extends BaseTimeEntity {

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

    @Column(name = "author", nullable = false)
    private String author;

    @Embedded
    @Column(name = "thumbnail_url")
    private Thumbnail thumbnailUrl;

    @ManyToOne
    private Menu menu;

    @Builder
    public Article(String title, String content, Thumbnail thumbnailUrl, String subTitle, String author, Menu menu) {
        this.title = title;
        this.content = content;
        this.subTitle = subTitle;
        this.author = author;
        this.menu = menu;
        this.thumbnailUrl = thumbnailUrl;
    }

    public void update(String title, String subTitle, String content, Thumbnail thumbnailUrl, Menu menu) {
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getThumbnailUrl() {
        if (Objects.isNull(thumbnailUrl)) {
            return new Thumbnail("").getUrl();
        }
        return thumbnailUrl.getUrl();
    }
}
