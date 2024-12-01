package me.kimyeonsup.home.domain.blog.admin.menu.repository;

import me.kimyeonsup.home.domain.blog.admin.menu.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
