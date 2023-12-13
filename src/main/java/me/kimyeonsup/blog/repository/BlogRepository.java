package me.kimyeonsup.blog.repository;

import me.kimyeonsup.blog.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Article, Long> {
}
