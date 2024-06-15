package me.kimyeonsup.home.domain.blog.menu.service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import me.kimyeonsup.home.domain.blog.menu.domain.dto.AddMenuRequest;
import me.kimyeonsup.home.domain.blog.menu.domain.dto.UpdateMenuRequest;
import me.kimyeonsup.home.domain.blog.menu.domain.dto.MenuResponse;
import me.kimyeonsup.home.domain.blog.menu.domain.entity.Category;
import me.kimyeonsup.home.domain.blog.menu.domain.entity.Menu;
import me.kimyeonsup.home.domain.blog.menu.repository.CategoryRepository;
import me.kimyeonsup.home.domain.blog.menu.repository.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final CategoryRepository categoryRepository;

    public MenuResponse save(AddMenuRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("not found: " + request.getCategoryId()));
        return new MenuResponse(menuRepository.save(request.toEntity(category)));
    }

    public List<Menu> findAll() {
        return menuRepository.findAll();
    }

    public void delete(long id) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));
        menuRepository.delete(menu);
    }

    @Transactional
    public MenuResponse update(long id, UpdateMenuRequest request) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("not found : " + request.getCategoryId()));
        menu.update(request.getName(), category);
        return new MenuResponse(menu);
    }
}
