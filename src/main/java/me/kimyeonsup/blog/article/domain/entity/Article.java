package me.kimyeonsup.blog.article.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kimyeonsup.blog.global.common.entity.BaseTimeEntity;
import me.kimyeonsup.blog.menu.domain.entity.Menu;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
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

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @ManyToOne
    private Menu menu;

    @Builder
    public Article(String title, String content, String subTitle, String author, Menu menu) {
        this.title = title;
        this.content = content;
        this.subTitle = subTitle;
        this.author = author;
        this.menu = menu;
        setThumbnailUrl(content);
    }


    public void update(String title, String content, long menuId) {
        this.title = title;
        this.content = content;
        setThumbnailUrl(content);
    }

    private void setThumbnailUrl(String content) {
        log.info("content: ${}", content);
        if (content.length() == 0) {
            return;
        }
        final int imgTagStartIndex = content.indexOf("![");
        if (imgTagStartIndex >= 0) {
            String imgUrl = content.substring(imgTagStartIndex + 4, content.indexOf(")", imgTagStartIndex + 4));
            this.thumbnailUrl = imgUrl;
        }
    }

}
