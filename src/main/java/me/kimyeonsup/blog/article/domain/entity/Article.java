package me.kimyeonsup.blog.article.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.kimyeonsup.blog.menu.domain.entity.Menu;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article {

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

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "author", nullable = false)
    private String author;

    @ManyToOne
    private Menu menu;

    @Builder
    public Article(String title, String content, String subTitle, String author, Menu menu) {
        this.title = title;
        this.content = content;
        this.subTitle = subTitle;
        this.author = author;
        this.menu = menu;
    }


    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
