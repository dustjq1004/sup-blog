package me.kimyeonsup.blog.menu.controller;

import lombok.RequiredArgsConstructor;
import me.kimyeonsup.blog.menu.domain.dto.AddMenuRequest;
import me.kimyeonsup.blog.menu.domain.dto.MenuResponse;
import me.kimyeonsup.blog.menu.domain.dto.UpdateMenuRequest;
import me.kimyeonsup.blog.menu.service.MenuService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MenuApiController {

    private final MenuService menuService;

    @PostMapping("/menu")
    public ResponseEntity<MenuResponse> addMenu(@RequestBody AddMenuRequest request) {
        MenuResponse savedCategory = menuService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedCategory);
    }

    @DeleteMapping("/menu/{id}")
    public ResponseEntity<Void> deleteMenu(@PathVariable long id) {
        menuService.delete(id);
        return ResponseEntity.ok()
                .build();
    }

    @PutMapping("/menu/{id}")
    public ResponseEntity<MenuResponse> updateMenu(@PathVariable long id,
                                                   @RequestBody UpdateMenuRequest request) {
        MenuResponse updatedCategory = menuService.update(id, request);

        return ResponseEntity.ok()
                .body(updatedCategory);
    }
}
