package me.kimyeonsup.blog.menu.repository;

import me.kimyeonsup.blog.menu.domain.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}
