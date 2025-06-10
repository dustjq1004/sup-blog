package me.kimyeonsup.home.domain.blog.admin.article.repository;

import me.kimyeonsup.home.domain.blog.admin.article.domain.entity.AdminArticle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminArticleRepository extends JpaRepository<AdminArticle, Long> {


    Page<AdminArticle> findByMenuIdAndTitleContaining(PageRequest pageRequest, Long menuId, String title);

    Page<AdminArticle> findByMenu_CategoryIdAndTitleContaining(PageRequest pageRequest, Long categoryId, String title);

    Page<AdminArticle> findByTitleContaining(PageRequest pageRequest, String title);
}
