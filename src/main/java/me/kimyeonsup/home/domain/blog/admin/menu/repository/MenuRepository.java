package me.kimyeonsup.home.domain.blog.admin.menu.repository;

import java.util.List;
import me.kimyeonsup.home.domain.blog.admin.menu.domain.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    List<Menu> findByCategoryId(Long categoryId);
}
