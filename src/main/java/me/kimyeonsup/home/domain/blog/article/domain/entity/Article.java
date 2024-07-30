package me.kimyeonsup.home.domain.blog.article.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.kimyeonsup.home.domain.blog.menu.domain.entity.Menu;
import me.kimyeonsup.home.global.common.entity.BaseTimeEntity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article extends BaseTimeEntity {
    private static final Pattern IMG_TAG_PATTERN = Pattern.compile("(?i)<img[^>]*src=[\"']?([^>\"']+)[\"']?[^>]*>");
    private static final Pattern MD_IMG_TAG_PATTERN = Pattern.compile("^!\\[[a-zA-Z0-9]*\\]\\(([\\w:.\\/-]*)");

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
        this.thumbnailUrl = getThumbnailUrl(content);
    }


    public void update(String title, String subTitle, String content, long menuId) {
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;
        this.thumbnailUrl = getThumbnailUrl(content);
    }

    // 1. img 태그 or ![]()

    private String getThumbnailUrl(String content) {
        if (content.isEmpty()) {
            return "";
        }

        Matcher match = IMG_TAG_PATTERN.matcher(content);
        if (match.find()) {
            return match.group(1);
        }

        match = MD_IMG_TAG_PATTERN.matcher(content);
        if (match.find()) {
            return match.group(1);
        }
        return "";
    }
}
