package me.kimyeonsup.home.domain.blog.admin.article.repository;

import java.util.List;
import me.kimyeonsup.home.domain.blog.admin.article.domain.entity.AdminArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminArticleRepository extends JpaRepository<AdminArticle, Long> {

    List<AdminArticle> findByCategoryId(Long categoryId);

    List<AdminArticle> findByMenuId(Long menuId);
}
