package me.kimyeonsup.blog.repository;

import me.kimyeonsup.blog.article.domain.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Article, Long> {
}
