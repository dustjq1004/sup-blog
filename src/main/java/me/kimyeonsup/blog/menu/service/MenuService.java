package me.kimyeonsup.blog.menu.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import me.kimyeonsup.blog.menu.domain.dto.AddMenuRequest;
import me.kimyeonsup.blog.menu.domain.dto.MenuResponse;
import me.kimyeonsup.blog.menu.domain.dto.UpdateMenuRequest;
import me.kimyeonsup.blog.menu.domain.entity.Menu;
import me.kimyeonsup.blog.menu.repository.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    public MenuResponse save(AddMenuRequest request) {
        return new MenuResponse(menuRepository.save(request.toEntity()));
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
        menu.update(request.getName(), request.getCategoryId());
        return new MenuResponse(menu);
    }
}
