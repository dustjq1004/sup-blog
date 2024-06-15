package me.kimyeonsup.home.domain.blog.article.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.kimyeonsup.home.global.common.entity.BaseTimeEntity;
import me.kimyeonsup.home.domain.blog.menu.domain.entity.Menu;

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
        if (content.length() == 0 || !isThereImage(content)) {
            return;
        }

        int startIndex = getThumbnailStartIndex(content);

        String imgUrl = content.substring(startIndex, content.indexOf(")", startIndex));
        this.thumbnailUrl = imgUrl;
    }

    private boolean isThereImage(String content) {
        int index = content.indexOf("[");

        if (index <= 0 || content.charAt(index - 1) != '!') {
            return false;
        }

        if (content.indexOf("]", index) < 0) {
            return false;
        }

        return true;
    }

    private int getThumbnailStartIndex(String content) {
        return content.indexOf("]", content.indexOf("![")) + 2;
    }

}
