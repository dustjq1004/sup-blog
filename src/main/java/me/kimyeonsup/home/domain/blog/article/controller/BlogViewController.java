package me.kimyeonsup.home.domain.blog.article.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import me.kimyeonsup.home.domain.blog.admin.menu.domain.dto.CategoryResponse;
import me.kimyeonsup.home.domain.blog.admin.menu.domain.dto.MenuResponse;
import me.kimyeonsup.home.domain.blog.admin.menu.service.CategoryService;
import me.kimyeonsup.home.domain.blog.admin.menu.service.MenuService;
import me.kimyeonsup.home.domain.blog.article.domain.dto.ArticlePrevNextResponse;
import me.kimyeonsup.home.domain.blog.article.domain.dto.ArticleResponse;
import me.kimyeonsup.home.domain.blog.article.domain.dto.ArticleUpdatedResponse;
import me.kimyeonsup.home.domain.blog.article.domain.entity.Article;
import me.kimyeonsup.home.domain.blog.article.service.ArticleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
@RequestMapping("/blog")
public class BlogViewController {

    private final ArticleService articleService;
    private final CategoryService categoryService;
    private final MenuService menuService;

    @GetMapping
    public String getAllArticles(Model model) {
        List<CategoryResponse> categories = categoryService.findAll().stream()
                .map(CategoryResponse::new)
                .toList();

        model.addAttribute("categories", categories);

        return "blog/index";
    }

    @GetMapping("/{menuName}")
    public String findArticlesByMenuName(@PathVariable String menuName, Model model) {
        List<CategoryResponse> categories = categoryService.findAll().stream()
                .map(CategoryResponse::new)
                .toList();

        MenuResponse menu = menuService.findByName(menuName);

        model.addAttribute("categories", categories);
        model.addAttribute("menu", menu);

        return "blog/index";
    }

    @GetMapping("/{menuName}/{id}")
    public String getArticle(@PathVariable String menuName, @PathVariable Long id, Model model) {
        List<CategoryResponse> categories = categoryService.findAll().stream()
                .map(CategoryResponse::new)
                .toList();
        Article article = articleService.findById(id);
        ArticlePrevNextResponse prevNextArticle = articleService.findPrevNextArticle(id, menuName);

        model.addAttribute("categories", categories);
        model.addAttribute("article", new ArticleResponse(article));
        model.addAttribute("prevNextArticle", prevNextArticle);

        return "blog/article";
    }

    @GetMapping("/new-article")
    public String newArticle(@RequestParam(required = false) Long id, Model model) {
        if (id == null) {
            model.addAttribute("article", new ArticleUpdatedResponse());
        } else {
            Article article = articleService.findById(id);
            model.addAttribute("article", new ArticleUpdatedResponse(article));
        }

        model.addAttribute("menus",
                menuService.findAll());

        return "blog/newArticle";
    }
}
