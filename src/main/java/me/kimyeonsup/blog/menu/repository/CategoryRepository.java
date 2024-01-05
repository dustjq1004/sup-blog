package me.kimyeonsup.blog.menu.repository;

import me.kimyeonsup.blog.menu.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
