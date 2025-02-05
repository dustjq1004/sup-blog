package me.kimyeonsup.home.domain.blog.admin.menu.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.kimyeonsup.home.domain.blog.admin.menu.domain.dto.UpdateMenuRequest;
import me.kimyeonsup.home.global.common.entity.BaseTimeEntity;

@Entity
@Getter
@RequiredArgsConstructor
public class Menu extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "emoji")
    private String emoji;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Builder
    public Menu(Long id, String name, String description, String emoji, Category category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.emoji = emoji;
        this.category = category;
    }


    public void update(UpdateMenuRequest menuRequest, Category category) {
        this.name = menuRequest.getName();
        this.description = menuRequest.getDescription();
        this.category = category;
    }
}
