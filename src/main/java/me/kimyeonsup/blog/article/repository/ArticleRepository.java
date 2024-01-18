package me.kimyeonsup.blog.article.repository;

import java.util.List;
import me.kimyeonsup.blog.article.domain.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    List<Article> findByMenuId(Long menuId);

    @Query("SELECT a FROM Article a LEFT JOIN FETCH a.menu m WHERE m.name = :menuName")
    List<Article> findByMenuName(String menuName);
}
