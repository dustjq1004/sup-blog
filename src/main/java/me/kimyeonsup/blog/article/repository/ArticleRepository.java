package me.kimyeonsup.blog.article.repository;

import java.util.List;
import me.kimyeonsup.blog.article.domain.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    List<Article> findByMenuId(Long menuId);
}
