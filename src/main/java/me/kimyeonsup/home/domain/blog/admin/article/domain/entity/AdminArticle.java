package me.kimyeonsup.home.domain.blog.admin.article.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import me.kimyeonsup.home.domain.blog.admin.menu.domain.entity.Menu;
import me.kimyeonsup.home.domain.blog.article.domain.vo.Thumbnail;
import me.kimyeonsup.home.global.common.entity.BaseTimeEntity;

@Entity
@Getter
@Table(name = "article")
public class AdminArticle extends BaseTimeEntity {

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

}
