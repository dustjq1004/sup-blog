package me.kimyeonsup.home.domain.blog.admin.article.repository;

import me.kimyeonsup.home.domain.blog.admin.article.domain.entity.AdminArticle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminArticleRepository extends JpaRepository<AdminArticle, Long> {


    Page<AdminArticle> findByMenuId(PageRequest pageRequest, Long menuId);

    Page<AdminArticle> findByMenu_CategoryId(Long categoryId);

}
