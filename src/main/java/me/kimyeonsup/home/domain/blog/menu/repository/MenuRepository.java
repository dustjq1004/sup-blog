package me.kimyeonsup.home.domain.blog.menu.repository;

import me.kimyeonsup.home.domain.blog.menu.domain.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}
